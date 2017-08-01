package com.emoge.app.emoge.ui.view;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;

/**
 * Created by jh on 17. 7. 27.
 * 서버 준비 용.
 * TODO : 서버준비화면. 사용 or 제거
 */

public class TextSplash {

    public static void play(TextSurface textSurface, AssetManager assetManager) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // TODO : font
//        final Typeface robotoBlack = Typeface.createFromAsset(assetManager, "fonts/Roboto-Black.ttf");
//        paint.setTypeface(robotoBlack);

        Text textEmoge = TextBuilder
                .create("Emoge")
                .setPaint(paint)
                .setSize(64)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.SURFACE_CENTER).build();

        Text textEditor = TextBuilder
                .create("Editor")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textEmoge).build();

        Text textForMotionImage = TextBuilder
                .create("for MOtion imaGE.")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.RIGHT_OF, textEditor).build();

        Text textMakeGif = TextBuilder
                .create("움짤 제작 !")
                .setPaint(paint)
                .setSize(74)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF, textForMotionImage).build();

        Text textHard = TextBuilder
                .create("어렵다?")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textMakeGif).build();

        Text textNo = TextBuilder
                .create("  No No ~~")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.WHITE)
                .setPosition(Align.RIGHT_OF, textHard).build();

        Text textEasy = TextBuilder
                .create("쉽게")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textNo).build();

        Text textMaking = TextBuilder
                .create("만들고")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textEasy).build();

        Text textSharing = TextBuilder
                .create("공유하자")
                .setPaint(paint)
                .setSize(44)
                .setAlpha(0)
                .setColor(Color.RED)
                .setPosition(Align.BOTTOM_OF | Align.CENTER_OF, textMaking).build();

        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textEmoge, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textEmoge, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textEmoge, 600, SideCut.show(Side.LEFT), false))),
                        new Parallel(new TransSurface(500, textEditor, Pivot.CENTER), ShapeReveal.create(textEditor, 1300, SideCut.show(Side.LEFT), false)),
                        Delay.duration(500),
                        new Parallel(new TransSurface(750, textForMotionImage, Pivot.CENTER), Slide.showFrom(Side.LEFT, textForMotionImage, 750), ChangeColor.to(textForMotionImage, 750, Color.WHITE)),
                        Delay.duration(500),
                        new Parallel(TransSurface.toCenter(textMakeGif, 500), Rotate3D.showFromSide(textMakeGif, 750, Pivot.TOP)),
                        new Parallel(TransSurface.toCenter(textHard, 500), Slide.showFrom(Side.TOP, textHard, 500)),
                        new Parallel(TransSurface.toCenter(textNo, 750), Slide.showFrom(Side.LEFT, textNo, 500)),
                        Delay.duration(500),
                        new Parallel(
                                new TransSurface(1500, textSharing, Pivot.CENTER),
                                new Sequential(
                                        new Sequential(ShapeReveal.create(textEasy, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(textMaking, 500, Circle.show(Side.CENTER, Direction.OUT), false)),
                                        new Sequential(ShapeReveal.create(textSharing, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                                )
                        ),
                        Delay.duration(200),
                        new Parallel(
                                ShapeReveal.create(textEasy, 1500, SideCut.hide(Side.LEFT), true),
                                new Sequential(Delay.duration(250), ShapeReveal.create(textMaking, 1500, SideCut.hide(Side.LEFT), true)),
                                new Sequential(Delay.duration(500), ShapeReveal.create(textSharing, 1500, SideCut.hide(Side.LEFT), true)),
                                Alpha.hide(textNo, 1500),
                                Alpha.hide(textHard, 1500)
                        )
                )
        );

    }


}
