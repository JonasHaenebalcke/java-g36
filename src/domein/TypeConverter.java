//package domein;
//
//import javax.persistence.AttributeConverter;
//import javax.persistence.Converter;
//import java.util.Arrays;
//
//@Converter(autoApply = true)
//public class TypeConverter implements AttributeConverter<TypeGebruiker, Object> {
//
//	@Override
//	public Object convertToDatabaseColumn(TypeGebruiker type) {
//		if (type == null /*|| type == typeg*/) {
//			return null;
//		}
//		return type.getCode();
//	}
//
//	@Override
//	public TypeGebruiker convertToEntityAttribute(Object code) {
//		return Arrays.stream(TypeGebruiker.values()).filter(c -> c.getCode().equals(code)).findFirst()
//				.orElseThrow(IllegalArgumentException::new);
//	}
//}