/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.codejava.my_spring_aplication;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author delacth
 */


public interface EmployeeRepository  extends JpaRepository<Employee, Long>{
    
}
