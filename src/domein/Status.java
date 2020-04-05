package domein;

import java.util.Arrays;

public enum Status {
	Actief(1), NietActief(0), Geblokkeerd(2);

	private int status;

	private Status(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
	
	public static Status of(int status) {
        return Arrays.stream(Status.values())
          .filter(p -> p.getStatus() == status)
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}