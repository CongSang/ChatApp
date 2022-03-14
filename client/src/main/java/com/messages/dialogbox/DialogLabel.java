package com.messages.dialogbox;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class DialogLabel extends Label {
    private DialogPos pos = DialogPos.FACE_LEFT_CENTER;
    private double pading = 10.0;
    private boolean systemCall = false;

    public DialogLabel() {
        super();
        init();
    }

    public DialogLabel(String arg0, Node arg1) {
        super(arg0, arg1);
        init();
    }

    public DialogLabel(String arg0) {
        super(arg0);
        init();
    }

    public DialogLabel(DialogPos dialogPos) {
        super();
        this.pos = dialogPos;
        init();
    }

    public DialogLabel(String arg0, Node arg1, DialogPos dialogPos) {
        super(arg0, arg1);
        this.pos = dialogPos;
        init();
    }

    public DialogLabel(String arg0, DialogPos dialogPos) {
        super(arg0);
        this.pos = dialogPos;
        init();
    }

    private void init() {
        DropShadow ds = new DropShadow();
        ds.setOffsetX(1.3);
        ds.setOffsetY(1.3);
        ds.setColor(Color.DARKGRAY);
        setPrefSize(Label.USE_COMPUTED_SIZE, Label.USE_COMPUTED_SIZE);
        shapeProperty().addListener((ObservableValue<? extends Shape> arg0, Shape arg1, Shape arg2) -> {
            if (systemCall) {
                systemCall = false;
            } else {
                shapeIt();
            }
        });

        heightProperty().addListener((Observable arg0) -> {
            if (!systemCall)
                setPrefHeight(Label.USE_COMPUTED_SIZE);
        });

        widthProperty().addListener((Observable observable) -> {
            if (!systemCall)
                setPrefHeight(Label.USE_COMPUTED_SIZE);
        });

        shapeIt();
    }

    @Override
    protected void updateBounds() {
        super.updateBounds();
        // top right bottom left
        switch (pos) {
            case FACE_LEFT_BOTTOM:
                setPadding(new Insets(pading, pading,
                        (this.getBoundsInLocal().getWidth() * ((Dialog) getShape()).drawRectBubbleIndicatorRule) / 2
                                + pading,
                        pading));
                break;
            case FACE_LEFT_CENTER:
                setPadding(new Insets(pading, pading, pading,
                        (this.getBoundsInLocal().getWidth() * ((Dialog) getShape()).drawRectBubbleIndicatorRule) / 2
                                + pading));
                break;
            case FACE_RIGHT_BOTTOM:
                setPadding(new Insets(pading,
                        (this.getBoundsInLocal().getWidth() * ((Dialog) getShape()).drawRectBubbleIndicatorRule) / 2
                                + pading,
                        pading, pading));
                break;
            case FACE_RIGHT_CENTER:
                setPadding(new Insets(pading,
                        (this.getBoundsInLocal().getWidth() * ((Dialog) getShape()).drawRectBubbleIndicatorRule) / 2
                                + pading,
                        pading, pading));
                break;
            case FACE_TOP:
                setPadding(new Insets(pading,
                        pading, pading, pading));
                break;
        }
    }

    public final double getPading() {
        return pading;
    }

    public void setPading(double pading) {
        if (pading > 25.0)
            return;
        this.pading = pading;
    }

    public DialogPos getDialogPos() {
        return pos;
    }

    public void setDialogPos(DialogPos dialogPos) {
        this.pos = dialogPos;
        shapeIt();
    }

    private void shapeIt() {
        systemCall = true;
        setShape(new Dialog(pos));
        System.gc();
    }

}
