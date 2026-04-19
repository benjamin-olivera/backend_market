package pe.com.market.repository.concepto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.market.enums.EstadoConcepto;
import pe.com.market.model.concepto.Concepto;

import java.util.List;

public interface ConceptoRepository extends JpaRepository<Concepto, Integer> {
    List<Concepto> findAllByOrderByNombreAsc();
    @Query("SELECT m FROM Concepto m WHERE " +
            "(:q IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Concepto> listarPaginado(@Param("q") String q, Pageable pageable);

    List<Concepto> findByEstadoOrderByNombreAsc(EstadoConcepto estado);
    long countByEstado(EstadoConcepto estado);
    boolean existsByNombreIgnoreCase(String nombre);
}
