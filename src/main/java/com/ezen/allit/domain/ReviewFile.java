package com.ezen.allit.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "review")
@Entity
public class ReviewFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String imageName;
    @ManyToOne
    @JoinColumn(name = "rvno")
    private Review review;
    @CreationTimestamp
    private Date regDate;
    
	public static ReviewFile toSaveReviewFile(Review review, String imageName) {
		ReviewFile reviewFile= new ReviewFile();
		reviewFile.setImageName(imageName);
		reviewFile.setReview(review);
		
		return reviewFile;
	}
}
