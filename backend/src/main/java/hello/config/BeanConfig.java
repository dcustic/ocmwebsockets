package hello.config;

import hello.data.User;
import hello.enums.UserState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class BeanConfig {

	@Bean("users")
	public Set<User> users(){
		return new HashSet<>();
	}


}
