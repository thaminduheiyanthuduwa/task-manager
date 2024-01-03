package com.taskmanager.task.repository;

import com.taskmanager.task.entity.AttendanceReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceReportRepository extends JpaRepository<AttendanceReportEntity,Integer> {
    List<AttendanceReportEntity> findAll();

    @Query(nativeQuery = true,
            value = "select * from hr_attendance where created_at > :date")
    List<AttendanceReportEntity> getAttendanceAfterDate(@Param("date") String date);

    @Query(nativeQuery = true,
            value = "select * from hr_attendance where created_at > :date and employee_id = :emp")
    List<AttendanceReportEntity> getAttendanceAfterDateForEmp(@Param("date") String date, @Param("emp") Integer emp);


    List<AttendanceReportEntity> findByEmployeeIdOrderByIdDesc(@Param("employeeId") int id);

    @Query(value = "select * from (select UUID() as unique_id, id, name_in_full, category, type, IFNULL(amount, 0) from(select * from (select pe.id, 2 as type_id,'Allowances' as category, pe.name_in_full, hca.name as type,  hra.amount   from people pe\n" +
            "left join hr_emp_param_configuration hec on pe.id = hec.employee_id\n" +
            "left join hr_emp_allowances hra on hra.employee_id = hec.id\n" +
            "left join hr_cmn_allowances hca on hca.id = hra.allowance_id order by pe.id) table1\n" +
            "UNION\n" +
            "select * from (select pe.id,1 as type_id,'Basic Salary' as category, pe.name_in_full, 'basic_salary' as type,  hec.basic_salary as amount from people pe\n" +
            "left join hr_emp_param_configuration hec on pe.id = hec.employee_id order by pe.id) table2\n" +
            "UNION\n" +
            "select * from (select pe.id, 3 as type_id, 'Deductions' as category, pe.name_in_full, hca.name as type,  hra.amount   from people pe\n" +
            "left join hr_emp_param_configuration hec on pe.id = hec.employee_id\n" +
            "left join hr_emp_deductions hra on hra.employee_id = hec.id\n" +
            "left join hr_deductions hca on hca.id = hra.deduction_id order by pe.id) table3\n" +
            "order by id, type_id) final_table\n" +
            "order by id) tab  ",
            nativeQuery = true)
    List<Object[]> getAllSalaryInfo();

    @Query(value = "SELECT distinct FSPS.id AS DT_RowId, FSPS.id, FSPS.installment_counter, FSPS.status,\n" +
            "FSPS.amount, FSPS.total_paid, (FSPS.amount - FSPS.total_paid) as due_amount, FSPS.due_date, FSPPC.student_id, FSPPC.batch_id,\n" +
            "S.name_initials, S.tel_mobile1, S.range_id, B.batch_name, BT.description AS student_type, installment_type\n" +
            "FROM `finance_student_payment_schedules` FSPS\n" +
            "JOIN finance_student_payment_plan_cards FSPPC ON FSPS.payment_plan_card_id = FSPPC.id AND FSPPC.status = 'APPROVED'\n" +
            "JOIN students S ON FSPPC.student_id = S.student_id\n" +
            "JOIN batch_student BS ON S.range_id = BS.student_id AND BS.status = 0 AND BS.batch_id = FSPPC.batch_id\n" +
            "JOIN batch_types as BT ON BS.batch_type = BT.id\n" +
            "JOIN batches B ON FSPPC.batch_id = B.batch_id\n" +
            "JOIN finance_student_fee_definitions FSFD ON FSPS.fee_definition_id = FSFD.id\n" +
            "WHERE FSPPC.status = 'APPROVED' and FSPS.status <> 'PAID' and FSPS.due_date <= '2023-06-16'\n" +
            "ORDER BY `FSPS`.`status`  DESC;\n", nativeQuery = true)
    Page<Map<String, Object>> gettingAllInfo(Pageable pageable);

    @Query(value = "SELECT distinct FSPS.id AS DT_RowId, FSPS.id, FSPS.installment_counter, FSPS.status,\n" +
            "FSPS.amount, FSPS.total_paid, (FSPS.amount - FSPS.total_paid) as due_amount, FSPS.due_date, FSPPC.student_id, FSPPC.batch_id,\n" +
            "S.name_initials, S.tel_mobile1, S.range_id, B.batch_name, BT.description AS student_type, installment_type\n" +
            "FROM `finance_student_payment_schedules` FSPS\n" +
            "JOIN finance_student_payment_plan_cards FSPPC ON FSPS.payment_plan_card_id = FSPPC.id AND FSPPC.status = 'APPROVED'\n" +
            "JOIN students S ON FSPPC.student_id = S.student_id\n" +
            "JOIN batch_student BS ON S.range_id = BS.student_id AND BS.status = 0 AND BS.batch_id = FSPPC.batch_id\n" +
            "JOIN batch_types as BT ON BS.batch_type = BT.id\n" +
            "JOIN batches B ON FSPPC.batch_id = B.batch_id\n" +
            "JOIN finance_student_fee_definitions FSFD ON FSPS.fee_definition_id = FSFD.id\n" +
            "WHERE FSPPC.status = 'APPROVED' and FSPS.status <> 'PAID' and FSPS.due_date <= '2023-06-16'\n" +
            "ORDER BY `FSPS`.`status`  DESC;\n", nativeQuery = true)
    List<Map<String, Object>> allInfo();

    @Query(value = "SELECT distinct si.full_name,  si.range_id as student_id, ba.batch_name, fs.due_date, fcfd.description,\n" +
            "fsp.plan_type, fs.amount,  fs.currency, fs.total_paid, fs.status, fs.installment_counter,\n" +
            "bt.description\n" +
            "FROM finance_student_payment_schedules fs\n" +
            "LEFT JOIN finance_student_fee_definitions fee ON fs.fee_definition_id = fee.id\n" +
            "LEFT JOIN finance_student_payment_plan_cards fsp ON fsp.id =  fs.payment_plan_card_id\n" +
            "LEFT JOIN students si ON si.student_id = fsp.student_id\n" +
            "LEFT JOIN finance_student_fee_definitions fsf ON fsf.fee_definition_id = fs.fee_definition_id\n" +
            "LEFT JOIN finance_batch_fee_types fbft ON fee.fee_definition_id = fbft.id\n" +
            "LEFT JOIN finance_common_fee_definition fcfd ON fcfd.id = fbft.common_fee_id\n" +
            "LEFT JOIN finance_batch_payment_plan_types fbp ON fbp.id = fsf.payment_plan_types_id\n" +
            "LEFT JOIN batches ba ON fsp.batch_id = ba.batch_id  \n" +
            "LEFT JOIN batch_student bss ON bss.student_id = si.student_old_id\n" +
            "LEFT JOIN batch_types bt ON bt.id = bss.batch_type\n" +
            "WHERE fs.due_date >= '2023-04-01' and fs.due_date < '2023-05-01' and bt.id <> 5\n" +
            "ORDER BY `fs`.`due_date`  DESC;\n", nativeQuery = true)
    List<Map<String, Object>> gettinIncomeInfo();

    @Query(value = "SELECT distinct si.full_name,  si.range_id as student_id, ba.batch_name, fs.due_date, fcfd.description,\n" +
            "fsp.plan_type, fs.amount,  fs.currency, fs.total_paid, fs.status, fs.installment_counter,\n" +
            "bt.description\n" +
            "FROM finance_student_payment_schedules fs\n" +
            "LEFT JOIN finance_student_fee_definitions fee ON fs.fee_definition_id = fee.id\n" +
            "LEFT JOIN finance_student_payment_plan_cards fsp ON fsp.id =  fs.payment_plan_card_id\n" +
            "LEFT JOIN students si ON si.student_id = fsp.student_id\n" +
            "LEFT JOIN finance_student_fee_definitions fsf ON fsf.fee_definition_id = fs.fee_definition_id\n" +
            "LEFT JOIN finance_batch_fee_types fbft ON fee.fee_definition_id = fbft.id\n" +
            "LEFT JOIN finance_common_fee_definition fcfd ON fcfd.id = fbft.common_fee_id\n" +
            "LEFT JOIN finance_batch_payment_plan_types fbp ON fbp.id = fsf.payment_plan_types_id\n" +
            "LEFT JOIN batches ba ON fsp.batch_id = ba.batch_id  \n" +
            "LEFT JOIN batch_student bss ON bss.student_id = si.student_old_id\n" +
            "LEFT JOIN batch_types bt ON bt.id = bss.batch_type\n" +
            "WHERE fs.due_date >= '2023-04-01' and fs.due_date < '2023-05-01' and bt.id <> 5\n" +
            "ORDER BY `fs`.`due_date`  DESC;\n", nativeQuery = true)
    Page<Map<String, Object>> gettingIncomeInfoPageination(Pageable pageable);

    @Query(value = "SELECT obj1.degree, obj1.batch_name, obj1.admission_number, obj1.full_name, obj1.registration_fee_paid, obj1.course_fee_paid,\n" +
            "obj1.initial_fee_paid, obj1.total_paid_amount, obj1.interest, (obj1.interest + obj1.total_paid_amount) as total_with_fine, obj2.latest_payment_date, obj2.latest_payment_status,  obj2.latest_paid_amount, obj1.ins_due_from, DATEDIFF(CURDATE(), ins_due_from) as no_of_days_outstanding, obj1.currency, \n" +
            "obj1.tel_mobile1 as mobile_number, obj1.kiu_mail\n" +
            "FROM (SELECT\n" +
            "            'TBA' as degree,\n" +
            "            ba.batch_name,\n" +
            "            s.range_id as admission_number,\n" +
            "            s.full_name, \n" +
            "            SUM(CASE WHEN (fd.installment_type = 'Registration' AND fs.status <> 'PAID') THEN fs.amount END) AS registration_fee_paid,\n" +
            "            SUM(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN fs.amount END) AS course_fee_paid,\n" +
            "            SUM(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN fs.amount END) AS initial_fee_paid,\n" +
            "            COALESCE(SUM(CASE WHEN (fd.installment_type = 'Registration' AND fs.status <> 'PAID') THEN fs.amount END), 0) +\n" +
            "            COALESCE(SUM(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN fs.amount END), 0) +\n" +
            "            COALESCE(SUM(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN fs.amount END), 0) as total_paid_amount,\n" +
            "            fs.late_payment as interest, MIN(fs.due_date) as ins_due_from, fs.currency, s.tel_mobile1, s.kiu_mail\n" +
            "        FROM\n" +
            "            finance_student_payment_schedules fs\n" +
            "        LEFT JOIN finance_student_fee_definitions fd ON fs.fee_definition_id = fd.id\n" +
            "        LEFT JOIN finance_student_payment_plan_cards fc ON fs.payment_plan_card_id = fc.id\n" +
            "        LEFT JOIN batch_student bs ON bs.student_id = fc.student_id\n" +
            "        LEFT JOIN students s ON s.range_id = fc.student_id\n" +
            "        LEFT JOIN batches ba ON bs.batch_id = ba.batch_id\n" +
            "        LEFT JOIN courses c ON c.course_id = ba.course_id\n" +
            "        LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
            "        WHERE\n" +
            "             s.range_id IS NOT NULL\n" +
            "            AND c.course_id NOT IN ('19')\n" +
            "            AND c.course_type_id NOT IN ('2')\n" +
            "            AND bt.id NOT IN ('4')\n" +
            "        GROUP BY\n" +
            "            s.range_id, ba.batch_name, admission_number, s.full_name\n" +
            "        HAVING \n" +
            "        \t MIN(fs.due_date) >= DATE_SUB(CURDATE(), INTERVAL :dateRange  DAY)\n" +
            "            AND MIN(fs.due_date) <= CURDATE()) obj1\n" +
            "            LEFT JOIN (\n" +
            "                SELECT\n" +
            "    fc.student_id,\n" +
            "    latest_payment.updated_at AS latest_payment_date,\n" +
            "    fs.status AS latest_payment_status,\n" +
            "    fs.total_paid AS latest_paid_amount\n" +
            "FROM\n" +
            "    finance_student_payment_plan_cards fc\n" +
            "LEFT JOIN (\n" +
            "    SELECT\n" +
            "        fs.payment_plan_card_id,\n" +
            "        MAX(fs.updated_at) AS updated_at\n" +
            "    FROM\n" +
            "        finance_student_payment_schedules fs\n" +
            "    WHERE\n" +
            "        fs.status IN ('PAID', 'PARTIAL')\n" +
            "    GROUP BY\n" +
            "        fs.payment_plan_card_id \n" +
            "    HAVING \n" +
            "    \tMIN(fs.due_date) >= DATE_SUB(CURDATE(), INTERVAL :dateRange DAY)\n" +
            "            AND MIN(fs.due_date) <= CURDATE()\n" +
            ") AS latest_payment ON fc.id = latest_payment.payment_plan_card_id\n" +
            "LEFT JOIN finance_student_payment_schedules fs ON fc.id = fs.payment_plan_card_id AND latest_payment.updated_at = fs.updated_at\n" +
            "WHERE\n" +
            "    fc.student_id IS NOT NULL AND fs.due_date IS NOT NULL\n" +
            "ORDER BY `latest_payment_date` DESC\n" +
            "\n" +
            "                ) obj2 ON obj2.student_id = obj1.admission_number  \n" +
            "ORDER BY `no_of_days_outstanding` ASC", nativeQuery = true)
    List<Map<String, Object>> fetchDueReports(Integer dateRange);

    @Query(value = "SELECT \n" +
            "fs.id,\n" +
            "s.full_name,\n" +
            " s.range_id as student_id,\n" +
            " c.course_name,\n" +
            " b.batch_name,\n" +
            " bt.description as student_category,\n" +
            " IF(s.reg_date = '0000-00-00', NULL, s.reg_date) as reg_date \n" +
            "FROM students s\n" +
            "            LEFT JOIN finance_student_payment_plan_cards fs ON s.student_id = fs.student_id\n" +
            "            LEFT JOIN batch_student bs ON bs.student_id = s.range_id\n" +
            "            LEFT JOIN batches b ON bs.batch_id = b.batch_id\n" +
            "            LEFT JOIN courses c ON c.course_id = b.course_id\n" +
            "            LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
            "            WHERE s.status = 1 \n" +
            "            AND fs.id IS NULL \n" +
            "            AND bt.id NOT IN ('4') \n" +
            "            AND b.course_id NOT IN ('19') \n" +
            "            AND c.course_type_id NOT IN ('2') \n" +
            "            group by s.student_id, b.batch_id", nativeQuery = true)
    List<Map<String, Object>> getStudentsWithoutPaymentCards();

    @Query(value = "SELECT\n" +
            "        std.range_id AS student_id,fsppc.id,\n" +
            "        std.name_initials,\n" +
            "    CAST(NULLIF(std.reg_date, '0000-00-00') AS CHAR) AS registered_date,\n" +
            "        fsfd.installment_type,\n" +
            "        fsps.installment_counter,\n" +
            "IF(fsps.status='',fsps.amount,((fsps.amount+fsps.tax_paid)-(fsps.total_paid-fsps.total_late_payment_paid))) as due_amount,\n" +
            "fsps.amount,\n" +
            "fsps.tax_paid,\n" +
            "fsps.total_paid,\n" +
            "fsps.total_late_payment_paid,\n" +
            "fsps.status as payment_status,\n" +
            "        b.batch_name,\n" +
            "      bt.description as student_category,\n" +
            "        c.course_name,\n" +
            "     NULLIF(fsps.due_date, '0000-00-00') as due_date,\n" +
            "        fsppc.status\n" +
            "    FROM\n" +
            "        finance_student_payment_plan_cards AS fsppc\n" +
            "        LEFT JOIN students AS std ON std.student_id = fsppc.student_id\n" +
            "        LEFT JOIN finance_student_fee_definitions fsfd ON fsppc.id = fsfd.payment_plan_card_id\n" +
            "        LEFT JOIN finance_student_payment_schedules fsps ON fsfd.id = fsps.fee_definition_id\n" +
            "        LEFT JOIN batch_student AS bs ON bs.student_inc_id = fsppc.student_id\n" +
            "        LEFT JOIN batches AS b ON b.batch_id = bs.batch_id\n" +
            "        LEFT JOIN courses AS c ON c.course_id = b.course_id\n" +
            "        LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
            "WHERE fsps.status NOT IN ('PAID')\n" +
            "AND c.course_id NOT IN ('19') \n" +
            "AND c.course_type_id NOT IN ('2')\n" +
            "AND bt.id NOT IN ('4')\n" +
            "AND fsps.due_date >= :startDate and fsps.due_date<=:endDate \n" +
            "AND fsppc.status='APPROVED'\n" +
            "group by fsppc.id, std.range_id,fsps.id",
            nativeQuery = true)
    List<Map<String, Object>> outStandingReport(String startDate, LocalDate endDate);


//    @Query(value =
//
//            "select \n" +
//            "\tstd.range_id as student_id,\n" +
//            "     NULLIF(std.reg_date, '0000-00-00') as registered_date,\n" +
//            "\tfsppc.status as payment_plan_cards_status,\n" +
//            "\top.amount,\n" +
//            "\top.total_paid,\n" +
//            "    IF(op.status='PENDING',op.amount,(op.amount-op.total_paid)) as due_amount,\n" +
//            "\top.status as payment_status,\n" +
//            "     NULLIF(op.due_date, '0000-00-00') as due_date,\n" +
//            "\tcm.name,\n" +
//            "    b.batch_name,\n" +
//            "\tc.course_name\n" +
//            "from \n" +
//            "\t\tfinance_student_payment_plan_cards as fsppc\n" +
//            "\t\tLEFT JOIN students AS std ON std.student_id = fsppc.student_id\n" +
//            "\t\tleft join finance_student_other_payments as op on fsppc.id=op.payment_plan_card_id\n" +
//            "\t\tleft join finance_common_other_payments as cm on op.common_payment_id=cm.id\n" +
//            "\t\tLEFT JOIN batch_student AS bs ON bs.student_inc_id = fsppc.student_id\n" +
//            "        LEFT JOIN batches AS b ON b.batch_id = bs.batch_id\n" +
//            "        LEFT JOIN courses AS c ON c.course_id = b.course_id\n" +
//            "        LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
//            "where c.course_id NOT IN ('19') \n" +
//            "\tand op.status NOT IN ('PAID') \n" +
//            "\tand op.due_date >= :startDate and op.due_date<=:currentDate \n" +
//            "\tand op.due_date IS NOT NULL \n" +
//            "\tand fsppc.status='APPROVED'\n"

    @Query(value =
            "SELECT \n" +
                    "    fsppc.id,\n" +
                    "    std.range_id AS student_id,\n" +
                    "    std.name_initials,\n" +
                    "    CAST(NULLIF(std.reg_date, '0000-00-00') AS CHAR) AS registered_date,\n" +
                    "    fsppc.status AS payment_plan_cards_status,\n" +
                    "    op.amount,\n" +
                    "    op.total_paid,\n" +
                    "    IF(op.status = 'PENDING', op.amount, (op.amount - op.total_paid)) AS due_amount,\n" +
                    "    op.status AS payment_status,\n" +
                    "    NULLIF(op.due_date, '0000-00-00') as due_date,\n" +
                    "    cm.name,\n" +
                    "    b.batch_name,\n" +
                    "    bt.description as student_category,\n" +
                    "    c.course_name\n" +
                    "FROM \n" +
                    "    finance_student_payment_plan_cards AS fsppc\n" +
                    "    LEFT JOIN students AS std ON std.student_id = fsppc.student_id\n" +
                    "    LEFT JOIN finance_student_other_payments AS op ON fsppc.id = op.payment_plan_card_id\n" +
                    "    LEFT JOIN finance_common_other_payments AS cm ON op.common_payment_id = cm.id\n" +
                    "    LEFT JOIN batch_student AS bs ON bs.student_inc_id = fsppc.student_id\n" +
                    "    LEFT JOIN batches AS b ON b.batch_id = bs.batch_id\n" +
                    "    LEFT JOIN courses AS c ON c.course_id = b.course_id\n" +
                    "    LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
                    "WHERE \n" +
                    "   op.status NOT IN ('PAID')\n" +
                    "AND c.course_id NOT IN ('19') \n" +
                    "AND c.course_type_id NOT IN ('2')\n" +
                    "AND bt.id NOT IN ('4')\n" +
                    "    AND op.due_date <= CURDATE() \n" +
                    "    AND fsppc.status = 'APPROVED'\n" +
                    "    AND op.due_date IS NOT NULL AND op.due_date <> '0000-00-00'\n" +
                    "    AND op.due_date >= :startDate" +
                    " group by fsppc.id, std.range_id,op.id"
            , nativeQuery = true)
    List<Map<String, Object>> outStandingOtherPaymentReport(String startDate);

    //    @Query(value = "SELECT\n" +
//            "    std.range_id AS student_id,\n" +
//            "    std.name_initials,\n" +
//            "    fsfd.installment_type,\n" +
//            "    fsps.installment_counter,\n" +
//            "\tfsfd.fee_amount,\n" +
//            "\tfsps.tax_paid,\n" +
//            "\tfsps.total_paid,\n" +
//            "\tfsps.total_late_payment_paid,\n" +
//            "    fsps.status as payment_status,\n" +
//            "     NULLIF(fsps.due_date, '0000-00-00') as due_date,\n" +
//            "    fsppc.status as payment_plan_cards_status,\n" +
//            "    b.batch_id,\n" +
//            "    b.batch_name,\n" +
//            "    c.course_id,\n" +
//            "    c.course_name,\n" +
//            "    d.dept_id,\n" +
//            "    d.dept_name,\n" +
//            "    f.faculty_id,\n" +
//            "    f.faculty_name\n" +
//            "FROM\n" +
//            "    finance_student_payment_plan_cards AS fsppc\n" +
//            "    LEFT JOIN students AS std ON std.student_id = fsppc.student_id\n" +
//            "    LEFT JOIN finance_student_fee_definitions fsfd ON fsppc.id = fsfd.payment_plan_card_id\n" +
//            "    LEFT JOIN finance_student_payment_schedules fsps ON fsfd.id = fsps.fee_definition_id\n" +
//            "    LEFT JOIN batch_student AS bs ON bs.student_inc_id = fsppc.student_id\n" +
//            "    LEFT JOIN batches AS b ON b.batch_id = bs.batch_id\n" +
//            "    LEFT JOIN courses AS c ON c.course_id = b.course_id\n" +
//            "    LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
//            "    LEFT JOIN departments AS d ON c.dept_id = d.dept_id\n" +
//            "    LEFT JOIN faculties AS f ON d.faculty_id = f.faculty_id\n" +
//            "WHERE\n" +
//            "    fsps.status NOT IN ('PAID')", nativeQuery = true)
    @Query(value = "SELECT\n" +
            "std.range_id AS student_id,\n" +
            "std.name_initials,fsppc.id,\n" +
            "CAST(NULLIF(std.reg_date, '0000-00-00') AS CHAR) AS registered_date,\n" +
            "fsfd.installment_type,\n" +
            "fsps.installment_counter,\n" +
            "IF(fsps.status='PAID',fsps.total_paid,((fsps.amount+fsps.tax_paid)-(fsps.total_paid-fsps.total_late_payment_paid))) as due_amount,\n" +
            "fsps.amount,\n" +
            "fsps.tax_paid,\n" +
            "fsps.total_paid,\n" +
            "fsps.total_late_payment_paid,\n" +
            "IF(fsps.status='', 'Pending',fsps.status) as payment_status,\n" +
            "NULLIF(fsps.due_date, '0000-00-00') as due_date,\n" +
            "fsppc.status,\n" +
            "bt.description as student_category,\n" +
            "b.batch_id,\n" +
            "b.batch_name,\n" +
            "c.course_id,\n" +
            "c.course_name,\n" +
            "d.dept_id,\n" +
            "d.dept_name,\n" +
            "f.faculty_id,\n" +
            "f.faculty_name\n" +
            "    FROM\n" +
            "        finance_student_payment_plan_cards AS fsppc\n" +
            "        LEFT JOIN students AS std ON std.student_id = fsppc.student_id\n" +
            "        LEFT JOIN finance_student_fee_definitions fsfd ON fsppc.id = fsfd.payment_plan_card_id\n" +
            "        LEFT JOIN finance_student_payment_schedules fsps ON fsfd.id = fsps.fee_definition_id\n" +
            "        LEFT JOIN batch_student AS bs ON bs.student_inc_id = fsppc.student_id\n" +
            "        LEFT JOIN batches AS b ON b.batch_id = bs.batch_id\n" +
            "        LEFT JOIN courses AS c ON c.course_id = b.course_id\n" +
            "        LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
            "LEFT JOIN departments AS d ON c.dept_id = d.dept_id\n" +
            "LEFT JOIN faculties AS f ON d.faculty_id = f.faculty_id\n" +
            "WHERE \n" +
            "c.course_id NOT IN ('19') \n" +
            "AND c.course_type_id NOT IN ('2')\n" +
            "AND bt.id NOT IN ('4')\n" +
            "AND fsppc.status='APPROVED'\n" +
            "AND fsps.due_date IS NOT NULL AND fsps.due_date <> '0000-00-00'\n" +
            "AND fsps.due_date >= :startDate and fsps.due_date<=:endDate  " +
            "group by fsppc.id, std.range_id,fsps.id\n"
            , nativeQuery = true)
    List<Map<String, Object>> getIncomeReport(String startDate, String endDate);

    @Query(value = "select \n" +
            "fsppc.id,\n" +
            "std.name_initials,\n" +
            "std.range_id as student_id,\n" +
            "fsppc.status as payment_plan_cards_status,\n" +
            "op.amount,\n" +
            "op.total_paid,\n" +
            "IF(op.status='', 'Pending' , op.status) as payment_status,\n" +
            "op.due_date,\n" +
            "cm.name,\n" +
            "bt.description as student_category,\n" +
            "b.batch_id,\n" +
            "b.batch_name,\n" +
            "c.course_id,\n" +
            "c.course_name,\n" +
            "d.dept_id,\n" +
            "d.dept_name,\n" +
            "f.faculty_id,\n" +
            "f.faculty_name\n" +
            "from \n" +
            "finance_student_payment_plan_cards as fsppc\n" +
            "LEFT JOIN students AS std ON std.student_id = fsppc.student_id\n" +
            "left join finance_student_other_payments as op on fsppc.id=op.payment_plan_card_id\n" +
            "left join finance_common_other_payments as cm on op.common_payment_id=cm.id\n" +
            "        LEFT JOIN batch_student AS bs ON bs.student_inc_id = fsppc.student_id\n" +
            "        LEFT JOIN batches AS b ON b.batch_id = bs.batch_id\n" +
            "        LEFT JOIN courses AS c ON c.course_id = b.course_id\n" +
            "        LEFT JOIN batch_types AS bt ON bt.id = bs.batch_type\n" +
            "LEFT JOIN departments AS d ON c.dept_id = d.dept_id\n" +
            "LEFT JOIN faculties AS f ON d.faculty_id = f.faculty_id\n" +
            "WHERE \n" +
            "c.course_id NOT IN ('19') \n" +
            "AND c.course_type_id NOT IN ('2')\n" +
            "AND bt.id NOT IN ('4')\n" +
            "and op.due_date <= :endDate \n" +
            "and fsppc.status='APPROVED'\n" +
            "AND op.due_date IS NOT NULL AND op.due_date <> '0000-00-00'\n" +
            "AND op.due_date >= :startDate" +
            " group by fsppc.id, std.range_id,op.id"
            , nativeQuery = true)
    List<Map<String, Object>> getIncomeReportOtherPayment(String startDate, String endDate);


//    @Query(value = "select obj2.admission_number,obj2.student_category, obj2.full_name, obj2.batch_name, obj3.drop_date, obj2.course_fee_due_installment AS outstanding_now_course_fee_due_installment, obj2.initial_fee_due_installment AS outstanding_now_initial_fee_due_installment,\n" +
//            "obj2.late_payment as outstanding_now_fine, \t0 as outstanding_now_other_payments, obj3.course_fee_due_installment AS outstanding_drop_course_fee_due_installment, obj3.initial_fee_due_installment AS outstanding_drop_initial_fee_due_installment,\n" +
//            "obj3.late_payment as outstanding_drop_fine, \t0 as outstanding_drop_other_payments, DATEDIFF(CURDATE(), obj3.drop_date) AS date_difference_in_days, obj3.drop_category\n" +
//            "from (SELECT\n" +
//            "            'TBA' as degree,\n" +
//            "            ba.batch_name,\n" +
//            "            ba.batch_id,\n" +
//            "            s.range_id as admission_number,\n" +
//            "            s.full_name,\n" +
//            "            bt.description as student_category,\n" +
//            "            COUNT(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS course_fee_due_installment,\n" +
//            "            COUNT(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS initial_fee_due_installment, SUM(fs.late_payment) as late_payment\n" +
//            "        FROM\n" +
//            "            finance_student_payment_schedules fs\n" +
//            "        LEFT JOIN finance_student_fee_definitions fd ON fs.fee_definition_id = fd.id\n" +
//            "        LEFT JOIN finance_student_payment_plan_cards fc ON fs.payment_plan_card_id = fc.id\n" +
//            "        LEFT JOIN batch_student bs ON bs.student_id = fc.student_id\n" +
//            "        LEFT JOIN students s ON s.range_id = fc.student_id\n" +
//            "        LEFT JOIN batches ba ON bs.batch_id = ba.batch_id\n" +
//            "        LEFT JOIN  batch_types bt ON bt.id=bs.batch_type\n" +
//            "        WHERE\n" +
//            "             s.range_id IS NOT NULL AND  fc.status IN ('TEMPORARY_DROP', 'INACTIVE', 'PERMANENT_DROP') AND\n" +
//            "\t\t\t fs.due_date >= :start_date AND fs.due_date <= :end_date\n" +
//            "        GROUP BY\n" +
//            "            s.range_id, ba.batch_name, ba.batch_id, admission_number, s.full_name) obj2\n" +
//            "            LEFT JOIN (SELECT \n" +
//            "    s.range_id,\n" +
//            "    ba.batch_id,\n" +
//            "    ba.batch_name,\n" +
//            "    COUNT(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS course_fee_due_installment,\n" +
//            "            COUNT(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS initial_fee_due_installment, SUM(fs.late_payment) as late_payment,\n" +
//            "    MAX(fsd.status_updated_on) as drop_date,\n" +
//            "    GROUP_CONCAT( DISTINCT fc.status SEPARATOR ', ') AS drop_category\n" +
//            "FROM\n" +
//            "    finance_student_payment_schedules fs\n" +
//            "LEFT JOIN finance_student_fee_definitions fd ON fs.fee_definition_id = fd.id\n" +
//            "LEFT JOIN finance_student_payment_plan_cards fc ON fs.payment_plan_card_id = fc.id\n" +
//            "LEFT JOIN batch_student bs ON bs.student_id = fc.student_id\n" +
//            "LEFT JOIN students s ON s.range_id = fc.student_id\n" +
//            "LEFT JOIN batches ba ON bs.batch_id = ba.batch_id\n" +
//            "LEFT JOIN courses c ON c.course_id = ba.course_id\n" +
//            "LEFT JOIN finance_student_drop_active_history fsd ON fsd.student_id = fc.student_id\n" +
//            "WHERE\n" +
//            "    s.range_id IS NOT NULL AND fc.status IN ('TEMPORARY_DROP', 'INACTIVE', 'PERMANENT_DROP') AND fs.due_date >= :start_date AND fs.due_date <= :end_date\n" +
//            "    AND fs.due_date <= fsd.status_updated_on and  c.course_id NOT IN ('19') or c.course_type_id NOT IN ('2')\n" +
//            "GROUP BY\n" +
//            "    s.range_id, ba.batch_name, ba.batch_id) obj3 ON obj2.admission_number = obj3.range_id AND obj2.batch_id = obj3.batch_id;", nativeQuery = true)
//    List<Map<String, Object>> getActiveToTemporaryDrop(String start_date, String end_date);


    @Query(value = "SELECT \n" +
            "    obj2.admission_number,\n" +
            "    obj2.student_category,\n" +
            "    obj2.full_name,\n" +
            "    obj2.batch_name,\n" +
            "    obj2.due_date,\n" +
            "    obj3.drop_date,\n" +
            "    obj2.course_fee_due_installment AS outstanding_now_course_fee_due_installment,\n" +
            "    obj2.initial_fee_due_installment AS outstanding_now_initial_fee_due_installment, \n" +
            "    obj2.late_payment AS outstanding_now_fine, \n" +
            "    0 AS outstanding_now_other_payments,\n" +
            "    obj3.course_fee_due_installment AS outstanding_drop_course_fee_due_installment,\n" +
            "    obj3.initial_fee_due_installment AS outstanding_drop_initial_fee_due_installment, \n" +
            "    obj3.late_payment AS outstanding_drop_fine, \n" +
            "    0 AS outstanding_drop_other_payments,\n" +
            "    DATEDIFF(CURDATE(), obj3.drop_date) AS date_difference_in_days,\n" +
            "    obj3.drop_category \n" +
            "FROM \n" +
            "    (\n" +
            "        SELECT \n" +
            "            'TBA' AS degree, \n" +
            "            ba.batch_name, \n" +
            "            ba.batch_id, \n" +
            "            s.range_id AS admission_number, \n" +
            "            s.full_name, \n" +
            "            fs.due_date,\n" +
            "            bt.description AS student_category, \n" +
            "            COUNT(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS course_fee_due_installment, \n" +
            "            COUNT(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS initial_fee_due_installment, \n" +
            "            SUM(fs.late_payment) AS late_payment \n" +
            "        FROM \n" +
            "            finance_student_payment_schedules fs \n" +
            "            LEFT JOIN finance_student_fee_definitions fd ON fs.fee_definition_id = fd.id \n" +
            "            LEFT JOIN finance_student_payment_plan_cards fc ON fs.payment_plan_card_id = fc.id \n" +
            "            LEFT JOIN batch_student bs ON bs.student_id = fc.student_id \n" +
            "            LEFT JOIN students s ON s.range_id = fc.student_id \n" +
            "            LEFT JOIN batches ba ON bs.batch_id = ba.batch_id \n" +
            "            LEFT JOIN courses c ON c.course_id = ba.course_id \n" +
            "            LEFT JOIN batch_types bt ON bt.id = bs.batch_type \n" +
            "        WHERE \n" +
            "            s.range_id IS NOT NULL \n" +
            "            AND fc.status IN ('TEMPORARY_DROP', 'INACTIVE', 'PERMANENT_DROP')\n" +
            "            AND c.course_id NOT IN ('19')\n" +
            "            AND c.course_type_id NOT IN ('2')\n" +
            "            AND bt.id NOT IN ('4')\n" +
            "        GROUP BY \n" +
            "            s.range_id, ba.batch_name, ba.batch_id, admission_number, s.full_name\n" +
            "    ) obj2 \n" +
            "LEFT JOIN \n" +
            "    (\n" +
            "        SELECT  \n" +
            "            s.range_id, \n" +
            "            ba.batch_id, \n" +
            "            ba.batch_name, \n" +
            "            COUNT(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS course_fee_due_installment, \n" +
            "            COUNT(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS initial_fee_due_installment, \n" +
            "            SUM(fs.late_payment) AS late_payment, \n" +
            "            MAX(fsd.status_updated_on) AS drop_date, \n" +
            "            GROUP_CONCAT(DISTINCT fc.status SEPARATOR ', ') AS drop_category \n" +
            "        FROM finance_student_payment_plan_cards fc\n" +
            "            LEFT JOIN students s ON s.range_id = fc.student_id \n" +
            "            LEFT JOIN finance_student_fee_definitions fd ON fc.id = fd.payment_plan_card_id\n" +
            "            LEFT JOIN finance_student_payment_schedules fs  ON fs.payment_plan_card_id = fc.id \n" +
            "            LEFT JOIN batch_student bs ON bs.student_id = fc.student_id \n" +
            "            LEFT JOIN batches ba ON bs.batch_id = ba.batch_id \n" +
            "            LEFT JOIN courses c ON c.course_id = ba.course_id \n" +
            "            LEFT JOIN batch_types bt ON bt.id = bs.batch_type \n" +
            "            LEFT JOIN finance_student_drop_active_history fsd ON fsd.student_id = fc.student_id \n" +
            "        WHERE \n" +
            "            s.range_id IS NOT NULL AND fc.status IN ('TEMPORARY_DROP', 'INACTIVE', 'PERMANENT_DROP')\n" +
            "            AND fs.due_date <= fsd.status_updated_on \n" +
            "            AND c.course_id NOT IN ('19')\n" +
            "            AND c.course_type_id NOT IN ('2')\n" +
            "            AND bt.id NOT IN ('4')\n" +
            "        GROUP BY \n" +
            "            s.range_id, ba.batch_name, ba.batch_id\n" +
            "    ) obj3 ON obj2.admission_number = obj3.range_id AND obj2.batch_id = obj3.batch_id\n" +
            "WHERE \n" +
            "    obj2.due_date >= :startDate \n" +
            "    AND obj2.due_date <= :endDate", nativeQuery = true)
    List<Map<String, Object>> getActiveToTemporaryDrop(String startDate, String endDate);


    String COMMON_SQL = "SELECT\n" +
            "    s.range_id AS admission_number,\n" +
            "    s.full_name,\n" +
            "    ba.batch_name,\n" +
            "    bt.description as student_category,\n" +
            "    s.nic_passport,\n" +
            "    (CASE\n" +
            "    WHEN bs.status=0 THEN 'Active'\n" +
            "    WHEN bs.status=1 THEN 'Transferred'\n" +
            "    WHEN bs.status=6 THEN 'Temporary Drop'\n" +
            "    WHEN bs.status=7 THEN 'Permanant Drop'\n" +
            "    WHEN bs.status=9 THEN 'Permanant Suspend'\n" +
            "    WHEN bs.status=10 THEN 'Temporary Suspend'\n" +
            "    WHEN bs.status=12 THEN 'Slo Inactive'\n" +
            "    WHEN bs.status=13 THEN 'Slo Drop'\n" +
            "END ) AS student_status," +
            "    SUM(CASE WHEN fd.installment_type = 'Registration' THEN fd.fee_amount END) AS registration_fee,\n" +
            "    SUM(CASE WHEN fd.installment_type = 'Course Fee' THEN fd.fee_amount END) AS course_fee,\n" +
            "    SUM(CASE WHEN fd.installment_type = 'Initial Fee' THEN fd.fee_amount END) AS initial_fee,\n" +
            "    SUM(discount.approved_amount) AS discount,\n" +
            "    COALESCE(SUM(CASE WHEN fd.installment_type = 'Registration' THEN fd.fee_amount END), 0) +\n" +
            "    COALESCE(SUM(CASE WHEN fd.installment_type = 'Course Fee' THEN fd.fee_amount END), 0) +\n" +
            "    COALESCE(SUM(CASE WHEN fd.installment_type = 'Initial Fee' THEN fd.fee_amount END), 0) AS total_final_amount,\n" +
            "    SUM(CASE WHEN (fd.installment_type = 'Registration' AND fs.status IN ('PAID', 'PARTIAL')) THEN fs.total_paid END) AS registration_fee_paid,\n" +
            "    SUM(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status IN ('PAID', 'PARTIAL')) THEN fs.total_paid END) AS course_fee_paid,\n" +
            "    SUM(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status IN ('PAID', 'PARTIAL')) THEN fs.total_paid END) AS initial_fee_paid,\n" +
            "    COALESCE(SUM(CASE WHEN (fd.installment_type = 'Registration' AND fs.status IN ('PAID', 'PARTIAL')) THEN fs.total_paid END), 0) +\n" +
            "    COALESCE(SUM(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status IN ('PAID', 'PARTIAL')) THEN fs.total_paid END), 0) +\n" +
            "    COALESCE(SUM(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status IN ('PAID', 'PARTIAL')) THEN fs.total_paid END), 0) AS total_paid_amount,\n" +
            "    SUM(CASE WHEN (fd.installment_type = 'Registration' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS registration_fee_outstanding,\n" +
            "            SUM(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS course_fee_outstanding,\n" +
            "            SUM(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END) AS initial_fee_outstanding,\n" +
            "            COALESCE(SUM(CASE WHEN (fd.installment_type = 'Registration' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END), 0) +\n" +
            "            COALESCE(SUM(CASE WHEN (fd.installment_type = 'Course Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END), 0) +\n" +
            "            COALESCE(SUM(CASE WHEN (fd.installment_type = 'Initial Fee' AND fs.status <> 'PAID') THEN ((fs.amount+ fs.tax_paid) - (fs.total_paid - fs.total_late_payment_paid)) END), 0) as total_outstanding_amount\n" +
            "FROM\n" +
            "    finance_student_payment_schedules fs\n" +
            "LEFT JOIN finance_student_fee_definitions fd ON fs.fee_definition_id = fd.id\n" +
            "LEFT JOIN finance_student_payment_plan_cards fc ON fs.payment_plan_card_id = fc.id\n" +
            "LEFT JOIN batch_student bs ON bs.student_id = fc.student_id\n" +
            "LEFT JOIN students s ON s.range_id = fc.student_id\n" +
            "LEFT JOIN batches ba ON bs.batch_id = ba.batch_id\n" +
            "LEFT JOIN courses c  ON c.course_id = ba.course_id\n" +
            "LEFT JOIN  batch_types bt ON bt.id=bs.batch_type\n" +
            "LEFT JOIN (SELECT fc.student_id , SUM(fbs.approved_amount) as approved_amount\n" +
            "FROM finance_student_payment_schedules fs\n" +
            "LEFT JOIN finance_student_fee_definitions fd ON fs.fee_definition_id = fd.id\n" +
            "LEFT JOIN finance_student_payment_plan_cards fc ON fs.payment_plan_card_id = fc.id\n" +
            "LEFT JOIN finance_student_payment_plans as fspp ON fspp.payment_plan_card_id = fc.id\n" +
            "left join finance_batch_scholarships  as fbs on fbs.id = fspp.scholarship_id\n" +
            "WHERE fbs.id IS NOT NULL AND fbs.finance_status = 'APPROVED'\n" +
            "GROUP BY fc.student_id) discount ON discount.student_id = fc.student_id\n";

    @Query(value = COMMON_SQL +
            "WHERE\n" +
            "    s.range_id IS NOT NULL\n" +
            "            AND c.course_id NOT IN ('19')\n" +
            "            AND c.course_type_id NOT IN ('2')\n" +
            "            AND bt.id NOT IN ('4')\n" +
            "AND ba.batch_id = :batchId\n" +
            "GROUP BY\n" +
            "    s.range_id, ba.batch_name, admission_number, s.full_name, s.nic_passport, student_status\n" +
            "HAVING \n" +
            "MIN(fs.due_date) <= CURDATE()", nativeQuery = true)
    List<Map<String, Object>> getFullPaymentDetailsWithBatch(Long batchId);

    @Query(value = COMMON_SQL +
            "WHERE\n" +
            "    s.range_id IS NOT NULL\n" +
            "            AND c.course_id NOT IN ('19')\n" +
            "            AND c.course_type_id NOT IN ('2')\n" +
            "            AND bt.id NOT IN ('4')\n" +
            "GROUP BY\n" +
            "    s.range_id, ba.batch_name, admission_number, s.full_name, s.nic_passport, student_status\n" +
            "HAVING \n" +
            "MIN(fs.due_date) <= CURDATE()", nativeQuery = true)
    List<Map<String, Object>> getFullPaymentDetails();
    @Query(value = "SELECT p.id, p.name_in_full,  f.faculty_name, d.dept_name,  ad.department_name, cat1.task_title, \n" +
            "(CASE WHEN cat1.category_id = 1 THEN 'Examination - Invigilating' \n" +
            "WHEN cat1.category_id = 2 THEN 'Lecturing - Preparation' \n" +
            "WHEN cat1.category_id = 3 THEN 'Other' \n" +
            "WHEN cat1.category_id = 4 THEN 'Research - Own' \n" +
            "WHEN cat1.category_id = 5 THEN 'Lecturing - Conducting' \n" +
            "WHEN cat1.category_id = 6 THEN 'Examination - Paper Preparing' \n" +
            "WHEN cat1.category_id = 7 THEN 'Examination - Paper Marking' \n" +
            "WHEN cat1.category_id = 8 THEN 'Research - Student' \n" +
            "ELSE 'Not Identified' END) AS category, cat1.task_description, cat1.start_date, cat1.end_date, cat1.label, \n" +
            "cat1.estimate, (CASE WHEN cat1.status = 1 THEN 'Pending' WHEN cat1.status = 3 THEN 'Completed' WHEN cat1.status = 5 THEN \n" +
            "'Supervisor Completed' ELSE 'Not Identified' END) AS status FROM (select * from task_list WHERE start_date >= :startDate \n" +
            "and start_date <= :endDate and user_id in (SELECT id FROM people WHERE person_type IN  ('lecturer'))\n" +
            "and status not in (2,4))as cat1 left join (select id, name_in_full, faculty_id, dept_id, admin_department_id from people) as p on p.id = cat1.user_id \n" +
            "left join faculties f on p.faculty_id = f.faculty_id left join departments d on p.dept_id = d.dept_id\n" +
            "left join admin_departments ad on p.admin_department_id = ad.admin_department_id",nativeQuery = true)
    List<Map<String,Object>> workSummaryReport(String startDate,String endDate);
}
