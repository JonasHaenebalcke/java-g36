package domein;

import java.util.Arrays;

public enum StatusSessie {
//	open, gesloten, InschrijvingenOpen, nietOpen;
	open(0), gesloten(1), InschrijvingenOpen(2), nietOpen(3);
	
	private int status;

	private StatusSessie(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
	
	public static StatusSessie of(int status) {
        return Arrays.stream(StatusSessie.values())
          .filter(p -> p.getStatus() == status)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
