package org.futurepages.core.validation;

public class ValidatorRobot {
//
//    /**
//     * Validate the fields of the object
//     *
//     * @param object
//     */
//    public static void validate(Object object) {
//		Class clss = object.getClass();
//        do{
//			Field[] fields = clss.getDeclaredFields();
//			for (Field field : fields) {
//				try {
//					field.setAccessible(true);
//                    if(field.isAnnotationPresent(InputField.class)){
//                        Object fieldValue = field.get(object);
//
//                        validationResult(fieldValue,
//                                         true,
//                                         field.getAnnotation(InputField.class),
//                                         field.getAnnotation(Column.class)
//                                        );
//                    }
//				} catch (ErrorException ex) {
//					throw ex;
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//			clss = clss.getSuperclass();
//		}while(clss.getSuperclass() != null);
//    }
//
//    public static HashMap<String,ErrorException> validationMap(Object object) {
//		Class clss = object.getClass();
//        HashMap<String,ErrorException> validationMap = new HashMap();
//        do{
//			Field[] fields = clss.getDeclaredFields();
//			for (Field field : fields) {
//				try {
//					field.setAccessible(true);
//                    if(field.isAnnotationPresent(InputField.class)){
//                        InputField inputField = field.getAnnotation(InputField.class);
//                        Object fieldValue = field.get(object);
//
//                        ErrorException ex = validationResult(fieldValue,
//                                                             false,
//                                                             inputField,
//                                                             field.getAnnotation(Column.class)
//                                        );
//                        String viewId = inputField.viewId().length() > 0 ? field.getName(): "";
//                        validationMap.put(viewId, ex);
//                    }
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//			clss = clss.getSuperclass();
//		}while(clss.getSuperclass() != null);
//        return validationMap;
//    }
//
//    private static ErrorException validationResult(Object fieldValue, boolean breakOnFirst, InputField inputField, Column column) throws InstantiationException, IllegalAccessException {
////        Validator validator = inputField.validator().newInstance();
////        validator.setInputField(inputField);
////        validator.setColumn(column);
////
////        ErrorException ex = validator.execute(fieldValue);
//
//        if(breakOnFirst){
//            throw ex;
//        }
//        return ex;
//    }
}