package net.javaguides.springboot.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.javaguides.springboot.model.Item;
import net.javaguides.springboot.repository.ItemRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.model.User;
import net.javaguides.springboot.repository.UserRepository;
import net.javaguides.springboot.web.dto.UserRegistrationDto;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(ItemRepository itemRepository) {
		super();
		this.itemRepository = itemRepository;
	}

	@Override
	public User save(UserRegistrationDto registrationDto, String role) {
		User user = new User(registrationDto.getFirstName(),
				registrationDto.getLastName(), registrationDto.getEmail(),
				passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role(role)));

		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	@Transactional
	public boolean purchaseProduct(long id) {
		Item item = itemRepository.findById(id);
		if (item == null) {
			return false;
		}
		long newQuantity = item.getAmount() - 1;
		if (newQuantity < 0) {
			return false;
		}
		item.setAmount(newQuantity);
		itemRepository.save(item);
		return true;
	}


}
