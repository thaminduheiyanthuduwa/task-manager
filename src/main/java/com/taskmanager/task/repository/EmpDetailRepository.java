package com.taskmanager.task.repository;

import com.taskmanager.task.entity.EmpDetailEntity;
import com.taskmanager.task.entity.PayrollEntityDetails;
import com.taskmanager.task.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpDetailRepository extends JpaRepository<EmpDetailEntity, Integer > {

    List<EmpDetailEntity> findAll();

    List<EmpDetailEntity> findByEmailAndNicNo(@Param("email") String email,
                                                  @Param("nicNo") String nicNo);

    List<EmpDetailEntity> findBySupervisor(@Param("supervisor") int supervisor);

    List<EmpDetailEntity> findByNameInFull(@Param("nameInFull") String nameInFull);

    @Query(nativeQuery = true,
            value = "SELECT * FROM `people` WHERE id in (258,261,263,266,269,270,271,272,273,275,276,277,274,257,279,288,291,293,301,306,313,314,319,363,292,420,296,425,464,468,480,488,492,493,512,527,516,517,554,583,584,597,499,654,664,666,668,671,673,42,50,38,37,36,54,55,58,49,44,103,99,69,91,71,94,553,105,130,161,165,172,169,184,369,383,370,395,372,394,431,439,444,451,452,453,342,462,487,402,484,483,491,477,510,343,507,526,547,556,576,422,578,585,588,380,596,598,599,647,667,710,704,709)")
    List<EmpDetailEntity> getEmpDetailListById();


}
