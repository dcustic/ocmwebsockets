package hello.data;

import hello.enums.UserState;

import java.util.Objects;

public class User {
	String name;
	UserState state;

	public User() {
	}

	public User(final String name, final UserState state) {
		this.name = name;
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public UserState getState() {
		return state;
	}

	public void setState(final UserState state) {
		this.state = state;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final User user = (User) o;
		return Objects.equals(name, user.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
