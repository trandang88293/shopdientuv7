package com.fpoly.duan.shopdientuv2.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.entitys.Address;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;
import com.fpoly.duan.shopdientuv2.jpa.AddressRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Lấy danh sách địa chỉ của tài khoản dựa trên username
    public List<Address> getAddressesByUsername(String username) throws Exception {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isPresent()) {
            return addressRepository.findByAccount(accountOpt.get());
        }
        throw new Exception("Không tìm thấy tài khoản với username: " + username);
    }

    // Thêm địa chỉ mới, lưu ý chỉ được thêm tối đa 3 địa chỉ cho mỗi tài khoản
    public Address addAddress(Address address, String username) throws Exception {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (!accountOpt.isPresent()) {
            throw new Exception("Không tìm thấy tài khoản với username: " + username);
        }
        Account account = accountOpt.get();
        List<Address> addresses = addressRepository.findByAccount(account);
        if (addresses.size() >= 3) {
            throw new Exception("Chỉ được thêm tối đa 3 địa chỉ cho tài khoản này");
        }
        address.setAccount(account);
        return addressRepository.save(address);
    }

    // Cập nhật địa chỉ, chỉ cho phép chỉnh sửa nếu địa chỉ thuộc về tài khoản
    public Address updateAddress(Address address, String username) throws Exception {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (!accountOpt.isPresent()) {
            throw new Exception("Không tìm thấy tài khoản với username: " + username);
        }
        Address existing = addressRepository.findById(address.getAddressId())
                .orElseThrow(() -> new Exception("Không tìm thấy địa chỉ với ID: " + address.getAddressId()));
        // Kiểm tra quyền sở hữu địa chỉ
        if (existing.getAccount().getAccountId() != accountOpt.get().getAccountId()) {
            throw new Exception("Địa chỉ không thuộc về tài khoản này");
        }

        existing.setRecipientName(address.getRecipientName());
        existing.setPhoneNumber(address.getPhoneNumber());
        existing.setProvinceId(address.getProvinceId());
        existing.setDistrictId(address.getDistrictId());
        existing.setWardId(address.getWardId());
        existing.setThirdPartyField(address.getThirdPartyField());
        return addressRepository.save(existing);
    }

    // Xóa địa chỉ, chỉ cho phép xóa nếu địa chỉ thuộc về tài khoản
    public void deleteAddress(Integer addressId, String username) throws Exception {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (!accountOpt.isPresent()) {
            throw new Exception("Không tìm thấy tài khoản với username: " + username);
        }
        Address existing = addressRepository.findById(addressId)
                .orElseThrow(() -> new Exception("Không tìm thấy địa chỉ với ID: " + addressId));
        if (existing.getAccount().getAccountId() != accountOpt.get().getAccountId()) {
            throw new Exception("Địa chỉ không thuộc về tài khoản này");
        }

        addressRepository.delete(existing);
    }

    public Address getAddressById(Integer addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

}