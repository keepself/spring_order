package com.encore.ordering.item.service;

import com.encore.ordering.item.Dto.ItemRequset;
import com.encore.ordering.item.Dto.ItemResponse;
import com.encore.ordering.item.Dto.ItemSearchDto;
import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.respository.ItemRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Predicates;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;

@Transactional
@Service
public class ItemService {


    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(ItemRequset itemRequset) {
        MultipartFile multipartFile = itemRequset.getItemImage();
        String fileName = multipartFile.getOriginalFilename();
        Item new_item = Item.builder()
                .name(itemRequset.getName())
                .category(itemRequset.getCategory())
                .price(itemRequset.getPrice())
                .stockQuantity(itemRequset.getStockQuantity())
                .build();
        Item item = itemRepository.save(new_item);
        Path path = Paths.get("C:/Users/Playdata/Desktop/TMP", item.getId() + "_" + fileName);
        item.setImagePath(path.toString());
        try {
            byte[] bytes = multipartFile.getBytes();
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image cannot available");
        }
        return item;
    }

    public Item delete(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found item"));
        item.deleteItem();
        return item;
    }

    public Resource getImage(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found item"));
        String imagePath = item.getImagePath();
        Path path = Paths.get(imagePath);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url form is not valid");
        }
        return resource;
    }

    public  Item update(Long id, ItemRequset itemRequset){
        Item item = itemRepository.findById(id).orElseThrow(()->new EntityNotFoundException("not found item"));
        MultipartFile multipartFile = itemRequset.getItemImage();
        String fileName = multipartFile.getOriginalFilename();
        Path path = Paths.get("C:/Users/Playdata/Desktop/TMP", item.getId() + "_" + fileName);
        item.updateItem(itemRequset.getName(),
                itemRequset.getCategory(), itemRequset.getPrice(),itemRequset.getStockQuantity(),path.toString());
        try {
            byte[] bytes = multipartFile.getBytes();
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalArgumentException("image cannot available");
        }
        return item;

    }
    public List<ItemResponse> findAll(ItemSearchDto itemSearchDto, Pageable pageable){
//        검색을 위해 Specification 객체 사용
//        Specification객체는 복잡한 쿼리를 명세를 이용한 정의하여 쉽게 생성
        Specification<Item> spec = new Specification<Item>() {
            @Override
//            root : 엔티티의 속성을 접근하기 위한 객체, builder는 쿼리를 생성하기 위한 객체
            public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(itemSearchDto.getName() !=null){
                    predicates.add(criteriaBuilder.
                            like(root.get("name"),"%" + itemSearchDto.getName() + "%"));
                }
                if(itemSearchDto.getCategory() !=null){
                    predicates.add(criteriaBuilder.
                            like(root.get("category"),"%" + itemSearchDto.getCategory() + "%"));
                }
                predicates.add(criteriaBuilder.equal(root.get("delYn"),"N"));
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for(int i=0; i< predicates.size(); i++){
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };

        Page<Item> items = itemRepository.findAll(spec, pageable);
        List<Item> itemList = items.getContent();
        List<ItemResponse> itemResponses = new ArrayList<>();
        itemResponses = itemList.stream().map(i-> ItemResponse.builder()
                .id(i.getId())
                .name(i.getName())
                .category(i.getCategory())
                .price(i.getPrice())
                .stockQuantity(i.getStockQuantity())
                .imagePath(i.getImagePath())
                .build()).collect(Collectors.toList());
        return itemResponses;

    }
}
