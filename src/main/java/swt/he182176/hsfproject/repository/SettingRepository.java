package swt.he182176.hsfproject.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swt.he182176.hsfproject.entity.Setting;

import java.util.List;
import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

    List<Setting> findByTypeIsNullAndStatusTrueOrderByNameAsc();

    Optional<Setting> findByNameIgnoreCaseAndTypeIsNull(String name);

    @Query("""
        select s
        from Setting s
        left join s.type t
        where s.type is not null
          and (:typeId is null or t.id = :typeId)
          and (:status is null or s.status = :status)
          and (
                :keyword is null
                or trim(:keyword) = ''
                or lower(s.name) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(s.value, '')) like lower(concat('%', :keyword, '%'))
              )
    """)
    List<Setting> search(
            @Param("typeId") Integer typeId,
            @Param("status") Boolean status,
            @Param("keyword") String keyword,
            Sort sort
    );

    @Query("""
        select case when count(s) > 0 then true else false end
        from Setting s
        where lower(s.name) = lower(:name)
          and (
                (:typeId is null and s.type is null)
                or (:typeId is not null and s.type.id = :typeId)
              )
          and (:excludeId is null or s.id <> :excludeId)
    """)
    boolean existsDuplicateName(
            @Param("name") String name,
            @Param("typeId") Integer typeId,
            @Param("excludeId") Integer excludeId
    );
}