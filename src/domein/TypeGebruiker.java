package domein;

import java.io.Serializable;
import java.util.Arrays;

public enum TypeGebruiker{
	/*,*/ Verantwoordelijke(0), Hoofdverantwoordelijke(1), Gebruiker(2);

//	Gebruiker,
//	Verantwoordelijke,
//	Hoofdverantwoordelijke

	private int value;

	private TypeGebruiker(int value) {
		this.value = value;
	}
	

	public int getValue() {
		return value;
	}

//	public static Type of(Object type) {
//		return Arrays.stream(Type.values()).filter(p -> p.getType() == type).findFirst()
//				.orElseThrow(IllegalArgumentException::new);
//	}
}