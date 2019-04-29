package hello.repositories;

import hello.data.User;
import hello.enums.UserState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class UserRepository {

	@Value("#{users}")
	private Set<User> users;

	public void createOrUpdateUser(User user){
		if(users.contains(user)){
			users.remove(user);
			users.add(user);
		}else{
			users.add(user);
		}
	}

	public Set<User> getUsers(){
		return users;
	}

}
