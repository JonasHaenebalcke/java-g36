package domein;

import java.io.Serializable;
import java.util.Arrays;

public enum TypeGebruiker{
	/*,*/ Verantwoordelijke(0), Hoofdverantwoordelijke(1), Gebruiker(null);

//	Gebruiker,
//	Verantwoordelijke,
//	Hoofdverantwoordelijke

	private Object code;

	private TypeGebruiker(Object code) {
		this.code = code;
	}
	

	public Object getCode() {
		return code;
	}

//	public static Type of(Object type) {
//		return Arrays.stream(Type.values()).filter(p -> p.getType() == type).findFirst()
//				.orElseThrow(IllegalArgumentException::new);
//	}
}