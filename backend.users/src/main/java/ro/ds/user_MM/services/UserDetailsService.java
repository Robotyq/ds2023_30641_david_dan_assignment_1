//package ro.ds.user_MM.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import ro.ds.user_MM.repositories.UserRepository;
//
//@Service
//public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return (UserDetails) userRepository.findByEmail(username)
//                .orElseThrow(()->
//                        new UsernameNotFoundException(String.format("User with email %s not found", username)));
//    }
//}
