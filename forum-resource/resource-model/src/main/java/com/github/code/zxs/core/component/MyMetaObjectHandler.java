package com.github.code.zxs.core.component;











@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired(required = false)
    private UserProvider userProvider;

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String CREATE_BY = "createBy";
    private static final String UPDATE_BY = "updateBy";

    /**
     * INSERT时填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object userIdentify = userProvider == null ? null : userProvider.getUserIdentify();
        fillValue(metaObject, CREATE_TIME, () -> getDateValue(metaObject.getSetterType(CREATE_TIME)));
        fillValue(metaObject, UPDATE_TIME, () -> getDateValue(metaObject.getSetterType(UPDATE_TIME)));
        fillValue(metaObject, CREATE_BY, () -> userIdentify);
        fillValue(metaObject, UPDATE_BY, () -> userIdentify);
    }

    /**
     * UPDATE时填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object userIdentify = userProvider == null ? null : userProvider.getUserIdentify();
        fillValue(metaObject, UPDATE_TIME, () -> getDateValue(metaObject.getSetterType(UPDATE_TIME)));
        fillValue(metaObject, UPDATE_BY, () -> userIdentify);
    }

    /**
     * 根据Supplier接口返回对象填充值，只覆盖null值
     *
     * @param metaObject
     * @param fieldName
     * @param valueSupplier
     */
    private void fillValue(MetaObject metaObject, String fieldName, Supplier<Object> valueSupplier) {
        if (!metaObject.hasGetter(fieldName)) {
            return;
        }
        Object sidObj = metaObject.getValue(fieldName);
        if (sidObj == null && metaObject.hasSetter(fieldName) && valueSupplier != null) {
            setFieldValByName(fieldName, valueSupplier.get(), metaObject);
        }
    }

    private Object getDateValue(Class<?> setterType) {
        if (Date.class.equals(setterType)) {
            return new Date();
        } else if (LocalDateTime.class.equals(setterType)) {
            return LocalDateTime.now();
        } else if (ZonedDateTime.class.equals(setterType)) {
            return ZonedDateTime.now();
        } else if (Long.class.equals(setterType)) {
            return System.currentTimeMillis();
        }
        return null;
    }
}