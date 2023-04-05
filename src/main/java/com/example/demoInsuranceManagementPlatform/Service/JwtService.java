package com.example.demoInsuranceManagementPlatform.Service;

import com.example.demoInsuranceManagementPlatform.JwtDto.JwtRequest;
import com.example.demoInsuranceManagementPlatform.JwtDto.JwtResponse;
import com.example.demoInsuranceManagementPlatform.Model.Client;
import com.example.demoInsuranceManagementPlatform.Repository.ClientRepository;
import com.example.demoInsuranceManagementPlatform.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String clientEmail = jwtRequest.getClientEmail();
        String clientPassword = jwtRequest.getClientPassword();
        authenticate(clientEmail,clientPassword);

        UserDetails userDetails= loadUserByUsername(clientEmail);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);
        return new JwtResponse(newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String clientEmail) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(clientEmail);

        if (client != null) {
            return new org.springframework.security.core.userdetails.User(
                    client.getClientEmail(),
                    client.getClientPassword(),
                    getAuthority(client)
            );
        } else {
            throw new UsernameNotFoundException("User not found with clientEmail: " + clientEmail);
        }
    }

    private Set getAuthority(Client client) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        client.getRoleEntitySet().forEach( roleEntity-> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity.getRoleName()));
        });
        return authorities;
    }


    private void authenticate(String clientEmail, String clientPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(clientEmail, clientPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}