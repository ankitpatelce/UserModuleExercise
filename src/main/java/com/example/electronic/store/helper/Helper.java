package com.example.electronic.store.helper;

import com.example.electronic.store.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    /*************************************************************************************
     * Here U and V are types -- it may any type like int,String etc.. or type of object
     * For Example. Lets consider User module
     *              U = User (User Entity Class type)
     *              V = UserDto ( User Dto Class type)
     *         List<User> allUsers = page.getContent();
     *         List<UserDto> allDtos = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
     *         PageableResponse<UserDto> response = new PageableResponse<>();
     *         response.setContent(allDtos);
     *         response.setPageNumber(page.getNumber());
     *         response.setPageSize(page.getSize());
     *         response.setTotalPages(page.getTotalPages());
     *         response.setTotalElements(page.getTotalElements());
     *         response.setLastPages(page.isLast());
     **********************************************************************************/
    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber()); // Here index Start from Zero
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLastPages(page.isLast());
        return response;
    }
}
