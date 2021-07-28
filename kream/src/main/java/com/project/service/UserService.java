package com.project.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.AddressDTO;
import com.project.model.MemberDTO;
import com.project.model.ProductDAO;
import com.project.model.ProductDTO;
import com.project.model.ProductImgDTO;
import com.project.model.ProductWishDTO;
import com.project.model.UserDAO;
import com.project.service.Hash;

@Service
public class UserService {
	
	@Autowired private UserDAO dao;
	@Autowired private ProductDAO proddao;
	
	public String checkPhoneNum(String phoneNumber) {
		int checkPnum = dao.checkPhoneNum(phoneNumber);
		if(checkPnum == 1) {
			return hideEmail(dao.getEmail(phoneNumber));
		}
		else {
			return "0";	
		}
	}
	
	// 디비에서 가져온 이메일 데이터 부분적으로 가리기
	public String hideEmail(String phoneNumber) {
		String newMail = "";
		String[] oriMailDomain = phoneNumber.split("@");
		
		for(int i = 0; i < oriMailDomain[0].length(); i++) {
			if(i == 0 || i == oriMailDomain[0].length()-1) {
				newMail += oriMailDomain[0].charAt(i);
			}
			else {
				newMail += "*";			
			}
		}
		newMail += "@" + oriMailDomain[1];
		System.out.println(newMail);
		return newMail;
	}

	// 주소지 등록
	public int regiAddress(AddressDTO dto) {
		
		int basicckCount = dao.checkedBasicck(dto.getMemberIdx());
		System.out.println(dto.getBasicck());
		// 로그인 유저의 주소록이 1개 이상인지 확인한다
		if(basicckCount > 0) {
			// 주소록이 1개 이상이고 서브밋하는 기본 배송지 값이 null인 경우 그냥 'n' 입력
			if(dto.getBasicck() == null) {
				dto.setBasicck("n");
				return dao.regiAddress(dto);
			}
			// 기본 배송지는 1개만 등록할 수 있도록
			// 디비에 등록된 주소록이 1개 이상이고 매개변수로 받아온 기본 배송지 값이 'y'인 경우
			// 기존에  basicck 값이 'y'인 컬럼을 'n'으로 교체한다.
			else {
				dto.setBasicck("y");
				dao.changeBasicck();
				return dao.regiAddress(dto);
			}
		}
		// 로그인 유저의 주소록이 하나도 없다면 현재 서브밋한 주소를 기본 배송지로 등록한다.
		else {
			dto.setBasicck("y");
			return dao.regiAddress(dto);
		}
	}

	// 주소록 가져오기
	public List<AddressDTO> getAddressList(int loginIdx) {
		List<AddressDTO> list = dao.getAddressList(loginIdx);
		int listIdx = 0;
		for(AddressDTO dto : list) {
			if(dto.getBasicck().equals("y")) {
				listIdx = list.indexOf(dto);
			}
		}
		Collections.swap(list, 0, listIdx);
		return list;
	}

	
	// 관심상품 가져오기
	public List<ProductWishDTO> getWishList(int loginIdx) {
		return proddao.getWishList(loginIdx);
	}

	// 이메일을 이용해 소셜로그인 구분
	public String checkSocialLog(String email) {
		return dao.checkSocialLog(email);
	}
	
	public int checkEmailExist(String email) {
		return dao.checkEmailExist(email);
	}

	public int modifyEmail(String newmail, String oldmail) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("newmail", newmail);
		map.put("oldmail", oldmail);
		return dao.modifyEmail(map);
	}

	public MemberDTO getUser(String newmail) {
		return dao.getUser(newmail);
	}

	// 비밀번호 변경시 기존 비밀번호 일치 여부 확인
	public int compareNowPw(String nowHashpw, String nowLogin) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("nowpw", nowHashpw);
		map.put("nowLogin", nowLogin);
		return dao.compareNowPw(map);
	}

	public int changeNewPw(String newHashpw, String nowLogin) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("newpw", newHashpw);
		map.put("nowLogin", nowLogin);
		return dao.changeNewPw(map);
	}

	public int changeName(String newName, String nowLogin) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("newName", newName);
		map.put("nowLogin", nowLogin);
		return dao.changeNewName(map);
	}

	public int deleteProfile(int idx) {
		return dao.deleteProfile(idx);
	}

	public int wishItemDelete(int wishIdx) {
		return dao.wishItemDelete(wishIdx);
	}


	


	
	
	
}