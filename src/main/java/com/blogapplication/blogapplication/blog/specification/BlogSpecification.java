package com.blogapplication.blogapplication.blog.specification;

import com.blogapplication.blogapplication.blog.entity.Blog;
import com.blogapplication.blogapplication.blog.enums.BlogSearchField;
import com.blogapplication.blogapplication.common.dto.SeacrhCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BlogSpecification implements Specification<Blog> {


    private List<SeacrhCriteria> criteriaList;

    public  BlogSpecification(List<SeacrhCriteria> criteriaList){
        this.criteriaList=criteriaList;
    }

    @Override
    public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicateList = new ArrayList<>();

        this.buildPredicate(root,criteriaBuilder,predicateList);

        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }


    private void buildPredicate(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList){



        for(SeacrhCriteria searchCriteria : criteriaList){

            BlogSearchField blogSearchField = BlogSearchField.valueOf(searchCriteria.getField());

            switch (blogSearchField){
                case TITLE:{
                    this.titleSearch(root,criteriaBuilder,predicateList,searchCriteria);
                    break;
                }
                case CREATEDAT:{
                    this.creationTimeSearch(root,criteriaBuilder,predicateList,searchCriteria);
                    break;
                }
                case CREATEDBY:{
                    this.creatorSearch(root,criteriaBuilder,predicateList,searchCriteria);
                    break;
                }
                case FEATURED:{
                    this.featureSearch(root,criteriaBuilder,predicateList,searchCriteria);
                    break;
                }
            }
        }


    }

    private void featureSearch(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){

        predicateList.add(criteriaBuilder.equal( root.get(searchCriteria.getField()),Boolean.parseBoolean(searchCriteria.getValue().toString())));

    }

    private void titleSearch(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){

        predicateList.add(criteriaBuilder.like( root.get(searchCriteria.getField()),"%"+String.valueOf(searchCriteria.getValue())+"%"));

    }

    private void creatorSearch(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){

        predicateList.add(criteriaBuilder.equal( root.get("createdBy").get("id"),Long.valueOf(searchCriteria.getValue().toString())));

    }

    private void creationTimeSearch(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){

        switch (searchCriteria.getOperation()){
            case GREATERTHANEQUAL:{
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo( root.get(searchCriteria.getField()), LocalDateTime.parse(searchCriteria.getValue().toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
                break;
            }

            case LESSTHANEQUAL:{
                predicateList.add(criteriaBuilder.lessThanOrEqualTo( root.get(searchCriteria.getField()), LocalDateTime.parse(searchCriteria.getValue().toString(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
                break;
            }
            default:{
                break;
            }
        }



    }







//    private void greaterThanEqual(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){
//        predicateList.add(criteriaBuilder.greaterThanOrEqualTo( root.get(searchCriteria.getField()),searchCriteria.getValue()));
//    }
//
//    private void lessThanEqual(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){
//        predicateList.add(criteriaBuilder.lessThanOrEqualTo( root.get(searchCriteria.getField()),searchCriteria.getValue()));
//    }
//
//    private void equal(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){
//        predicateList.add(criteriaBuilder.equal( root.get(searchCriteria.getField()),searchCriteria.getValue()));
//    }
//
//    private void like(Root<Blog> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicateList,SeacrhCriteria searchCriteria){
//        predicateList.add(criteriaBuilder.like( root.get(searchCriteria.getField()),searchCriteria.getValue()));
//    }
}
