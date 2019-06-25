package com.ughen.constants;

/**
 * Description:
 * User: Yonghong Zhang
 * Date: 2019-01-23
 * Time: 13:47
 */
public enum QiNiuZone {

    /**
     * 华东
     */
    z0("z0"),
    /**
     * 华北
     */
    z1("z1"),
    /**
     * 华南
     */
    z2("z2"),
    /**
     * 北美
     */
    na0("na0"),
    /**
     * 东南亚
     */
    as0("as0");

    QiNiuZone() {

    }

    String value;

    QiNiuZone(String value) {
        this.value = value;
    }

    /**
     * 根据类型的名称,返回类型的枚举实例
     *
     * @param value 名称
     */
    public static QiNiuZone fromQiNiuZoneName(String value) {
        for (QiNiuZone type : QiNiuZone.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
