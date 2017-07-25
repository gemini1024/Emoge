#include <jni.h>
#include <android/log.h>
#include "com_emoge_app_emoge_ui_CameraActivity.h"

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace cv;

extern "C" {
    JNIEXPORT void JNICALL Java_com_emoge_app_emoge_ui_CameraActivity_ConvertRGBtoGray
      (JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

        const static char* logTag = "libnav";

        Mat &matInput = *(Mat *)matAddrInput;
        Mat &matResult = *(Mat *)matAddrResult;

//        cvtColor(matInput, matResult, CV_RGBA2GRAY);
//        equalizeHist(matResult, matResult);
        __android_log_print(ANDROID_LOG_DEBUG, logTag, "native process");

        unsigned char lut[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = saturate_cast<uchar>(pow((float)(i / 255.0), 0.5 /*Gamma*/) * 255.0f);
        }
        matResult = matInput.clone();
        MatIterator_<Vec3b> vit, vend;
        for (vit = matResult.begin<Vec3b>(), vend = matResult.end<Vec3b>(); vit != vend; vit++) {
            (*vit)[0] = lut[((*vit)[0])];
            (*vit)[1] = lut[((*vit)[1])];
            (*vit)[2] = lut[((*vit)[2])];
        }


//        const int channels = matResult.channels();
//        switch (channels) {
//            case 1:
//                MatIterator_<uchar> it, end;
//                for (it = matResult.begin<uchar>(), end = matResult.end<uchar>(); it != end; it++)
//                    *it = lut[(*it)];
//                break;
//            case 3:
//                MatIterator_<Vec3b> vit, vend;
//                for (vit = matResult.begin<Vec3b>(), vend = matResult.end<Vec3b>(); vit != vend; it++) {
//                    (*vit)[0] = lut[((*vit)[0])];
//                    (*vit)[1] = lut[((*vit)[1])];
//                    (*vit)[2] = lut[((*vit)[2])];
//                }
//                break;
//        }

    }

}