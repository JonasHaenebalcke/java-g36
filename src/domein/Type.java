package domein;

import java.util.Arrays;

public enum Type {
//	Gebruiker(null),
//	Verantwoordelijke(0),
//	Hoofdverantwoordelijke(1);
	
	Gebruiker,
	Verantwoordelijke,
	Hoofdverantwoordelijke
	
//	private Object type;
//
//	private Type(Object type) {
//		this.type = type;
//	}
//
//	public Object getType() {
//		return type;
//	}
//	
//	public static Type of(Object type) {
//        return Arrays.stream(Type.values())
//          .filter(p -> p.getType() == type)
//          .findFirst()
//          .orElseThrow(IllegalArgumentException::new);
//    }
}