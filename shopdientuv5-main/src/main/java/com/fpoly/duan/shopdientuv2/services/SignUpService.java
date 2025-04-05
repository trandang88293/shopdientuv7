package com.fpoly.duan.shopdientuv2.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.entitys.Cart;
import com.fpoly.duan.shopdientuv2.jpa.AccountJPA;

@Service
public class SignUpService {

        private final AccountJPA accountJPA;

        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        public SignUpService(AccountJPA accountJPA, BCryptPasswordEncoder bCryptPasswordEncoder) {
                this.accountJPA = accountJPA;
                this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        }

        public boolean signup(Account account, String cfpassword) {
                if (!account.getPassword().equals(cfpassword)) {
                        return false;
                }
                account.setPassword(bCryptPasswordEncoder.encode(cfpassword));
                Account save = accountJPA.save(account);
                Cart newCart = new Cart();
                newCart.setAccount(save);
                return true;
        }
}
