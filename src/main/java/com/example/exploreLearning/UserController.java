package com.example.exploreLearning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@RequestMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
	}
	
	@RequestMapping(value = "addUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserEntity> addUser( @RequestBody UserEntity user ) {

		if( userRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).size() > 0 ) {
			// user already exists, cannot add again
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userRepository.save(userEntity);

		return new ResponseEntity<>(userEntity, new HttpHeaders(), HttpStatus.OK);
	}

	@RequestMapping(value = "getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserEntity> getById( @RequestParam(value="id", defaultValue = "-1") Long id) {
		try {
			UserEntity user = userRepository.findById(id).orElse(null);
			if( user == null ) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
		}
		catch( Exception ex ) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserEntity>> getAllUsers() {
		try {
			List<UserEntity> users = userRepository.findAllByOrderByLastName();
			if( users.size() == 0 ) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(users, new HttpHeaders(), HttpStatus.OK);
		}
		catch( Exception ex ) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "deleteUser", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserEntity> deleteUser( @RequestParam(value="id", defaultValue = "-1") Long id) {
		try {
			UserEntity user = userRepository.findById(id).orElse(null);
			if( user == null ) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			userRepository.delete(user);

			return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
		}
		catch( Exception ex ) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
