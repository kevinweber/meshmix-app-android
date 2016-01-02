package com.meshmix.meshmix;

public enum AutoplayStatusEnum {
    AUTOPLAY_OFF(""+R.string.autoplaystatus_off),
    AUTOPLAY_ON(""+R.string.autoplaystatus_on),
    AUTOPLAY_SKIP_ONCE(""+R.string.autoplaystatus_skip);

    final String text;

    AutoplayStatusEnum(final String text) {
        AutoplayStatus.setStatus(this);
        this.text = text;
    }

    protected String getText() {
        return text;
    }
}
