package domein;

import java.util.HashMap;
import java.util.Map;

public enum Maand {
	Januari(1),
	Februari(2),
	Maart(3),
	April(4),
	Mei(5),
	Juni(6),
	Juli(7),
	Augustus(8),
	September(9),
	Oktober(10),
	November(11),
	December(12);
	
    private int value;
    private static Map map = new HashMap<>();

    private Maand(int value) {
        this.value = value;
    }

    static {
        for (Maand maand : Maand.values()) {
            map.put(maand.value, maand);
        }
    }

    public static Maand valueOf(int maand) {
        return (Maand) map.get(maand);
    }

    public int getValue() {
		return value;
	}
}
