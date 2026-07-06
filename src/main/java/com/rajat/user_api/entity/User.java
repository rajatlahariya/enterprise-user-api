package com.rajat.user_api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	  
	  private String username;

	  private String password;

	  private String role;

	  private String firstName;

	    private String lastName;

	    private String email;

	    private Integer age;
	    
	    private Boolean isActive;
	    
	    @CreationTimestamp
	    private LocalDateTime createdAt;
	    
	    public void testMethod() {
	        System.out.println(this.getFirstName());
	    }
	    
}
