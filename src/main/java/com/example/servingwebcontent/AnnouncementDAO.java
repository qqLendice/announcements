package com.example.servingwebcontent;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Repository
public class AnnouncementDAO {
	
	private final SessionFactory sessionFactory;

    @Autowired
    public AnnouncementDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
//    public List<Announcement> getAllAnnouncements() {
//        Session session = sessionFactory.getCurrentSession();
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        CriteriaQuery<Announcement> criteriaQuery = criteriaBuilder.createQuery(Announcement.class);
//        Root<Announcement> root = criteriaQuery.from(Announcement.class);
//        criteriaQuery.select(root);
//        TypedQuery<Announcement> query = session.createQuery(criteriaQuery);
//        return query.getResultList();
//    }
    
    public Page<Announcement> getAllAnnouncements(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        
        // Count total announcements
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Announcement.class)));
        Long totalItems = session.createQuery(countQuery).getSingleResult();

        // Fetch announcements with pagination
        CriteriaQuery<Announcement> criteriaQuery = criteriaBuilder.createQuery(Announcement.class);
        Root<Announcement> root = criteriaQuery.from(Announcement.class);
        criteriaQuery.select(root);

        TypedQuery<Announcement> query = session.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset()); // Offset is the starting index of the page
        query.setMaxResults(pageable.getPageSize()); // Page size
        List<Announcement> announcements = query.getResultList();

        return new PageImpl<>(announcements, pageable, totalItems);
    }
    

    @Transactional
    public void save(Announcement item) {
        Session session = sessionFactory.getCurrentSession();
        session.save(item);
        session.flush();
    }

    @Transactional
    public Announcement getById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Announcement.class, id);
    }
    
    @Transactional
    public void deleteAnnouncementById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Announcement announcementToDelete = session.get(Announcement.class, id);
        if (announcementToDelete != null) {
            session.delete(announcementToDelete);
        }
    }
    
}
