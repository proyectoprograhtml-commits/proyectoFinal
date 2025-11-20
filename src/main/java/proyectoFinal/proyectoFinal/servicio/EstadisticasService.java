package proyectoFinal.proyectoFinal.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyectoFinal.proyectoFinal.modelo.cita;
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.repositorio.citaRepositorio;
import proyectoFinal.proyectoFinal.repositorio.usuarioRepositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class EstadisticasService {

    @Autowired
    private usuarioRepositorio usuarioRepositorio;

    @Autowired
    private citaRepositorio citaRepositorio;

    // ========= MÉTRICAS GENERALES =========

    public long totalUsuarios() {
        return usuarioRepositorio.count();
    }

    public long totalPacientes() {
        return usuarioRepositorio.countByTipoUsuario(usuario.TipoUsuario.PACIENTE);
    }

    public long totalProfesionales() {
        return usuarioRepositorio.countByTipoUsuario(usuario.TipoUsuario.PROFESIONAL);
    }

    public long totalAdministradores() {
        return usuarioRepositorio.countByTipoUsuario(usuario.TipoUsuario.ADMINISTRADOR);
    }

    public long totalCitas() {
        return citaRepositorio.count();
    }

    /**
     * Citas del día actual (usado en el panel principal).
     */
    public List<cita> citasDeHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(LocalTime.MAX);
        return citaRepositorio.findByFechaHoraBetween(inicio, fin);
    }

    // ========= DTO PARA RESUMEN MENSUAL =========

    public static class ResumenMensual {

        private int anio;
        private int mes;

        private long totalCitas;
        private long citasProgramadas;
        private long citasCompletadas;
        private long citasCanceladas;

        private long nuevosUsuarios;
        private long nuevosPacientes;
        private long nuevosProfesionales;
        private long nuevosAdministradores;

        private Map<Integer, Long> citasPorSemana = new HashMap<>();

        public int getAnio() {
            return anio;
        }

        public void setAnio(int anio) {
            this.anio = anio;
        }

        public int getMes() {
            return mes;
        }

        public void setMes(int mes) {
            this.mes = mes;
        }

        public long getTotalCitas() {
            return totalCitas;
        }

        public void setTotalCitas(long totalCitas) {
            this.totalCitas = totalCitas;
        }

        public long getCitasProgramadas() {
            return citasProgramadas;
        }

        public void setCitasProgramadas(long citasProgramadas) {
            this.citasProgramadas = citasProgramadas;
        }

        public long getCitasCompletadas() {
            return citasCompletadas;
        }

        public void setCitasCompletadas(long citasCompletadas) {
            this.citasCompletadas = citasCompletadas;
        }

        public long getCitasCanceladas() {
            return citasCanceladas;
        }

        public void setCitasCanceladas(long citasCanceladas) {
            this.citasCanceladas = citasCanceladas;
        }

        public long getNuevosUsuarios() {
            return nuevosUsuarios;
        }

        public void setNuevosUsuarios(long nuevosUsuarios) {
            this.nuevosUsuarios = nuevosUsuarios;
        }

        public long getNuevosPacientes() {
            return nuevosPacientes;
        }

        public void setNuevosPacientes(long nuevosPacientes) {
            this.nuevosPacientes = nuevosPacientes;
        }

        public long getNuevosProfesionales() {
            return nuevosProfesionales;
        }

        public void setNuevosProfesionales(long nuevosProfesionales) {
            this.nuevosProfesionales = nuevosProfesionales;
        }

        public long getNuevosAdministradores() {
            return nuevosAdministradores;
        }

        public void setNuevosAdministradores(long nuevosAdministradores) {
            this.nuevosAdministradores = nuevosAdministradores;
        }

        public Map<Integer, Long> getCitasPorSemana() {
            return citasPorSemana;
        }

        public void setCitasPorSemana(Map<Integer, Long> citasPorSemana) {
            this.citasPorSemana = citasPorSemana;
        }
    }

    // ========= LÓGICA DEL INFORME MENSUAL =========

    /**
     * Calcula el resumen mensual de citas y usuarios nuevos para el año/mes indicados.
     */
    public ResumenMensual obtenerResumenMensual(int anio, int mes) {

        // Rango de fechas del mes [inicio, fin)
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.plusMonths(1);

        LocalDateTime inicioDt = inicio.atStartOfDay();
        LocalDateTime finDt = fin.atStartOfDay();

        ResumenMensual r = new ResumenMensual();
        r.setAnio(anio);
        r.setMes(mes);

        // 1) Citas del mes
        List<cita> citasMes = citaRepositorio.findByFechaHoraBetween(inicioDt, finDt);
        r.setTotalCitas(citasMes.size());

        long prog = citasMes.stream()
                .filter(c -> "programada".equalsIgnoreCase(c.getEstado()))
                .count();
        long comp = citasMes.stream()
                .filter(c -> "completada".equalsIgnoreCase(c.getEstado()))
                .count();
        long canc = citasMes.stream()
                .filter(c -> "cancelada".equalsIgnoreCase(c.getEstado()))
                .count();

        r.setCitasProgramadas(prog);
        r.setCitasCompletadas(comp);
        r.setCitasCanceladas(canc);

        // 2) Nuevos usuarios del mes (por fechaRegistro)
        long totalNuevos = usuarioRepositorio
                .countByFechaRegistroBetween(inicioDt, finDt);
        r.setNuevosUsuarios(totalNuevos);

        long nuevosPac = usuarioRepositorio
                .countByTipoUsuarioAndFechaRegistroBetween(
                        usuario.TipoUsuario.PACIENTE, inicioDt, finDt);
        long nuevosProf = usuarioRepositorio
                .countByTipoUsuarioAndFechaRegistroBetween(
                        usuario.TipoUsuario.PROFESIONAL, inicioDt, finDt);
        long nuevosAdmin = usuarioRepositorio
                .countByTipoUsuarioAndFechaRegistroBetween(
                        usuario.TipoUsuario.ADMINISTRADOR, inicioDt, finDt);

        r.setNuevosPacientes(nuevosPac);
        r.setNuevosProfesionales(nuevosProf);
        r.setNuevosAdministradores(nuevosAdmin);

        // 3) Citas por semana del mes (cálculo en Java)
        Map<Integer, Long> porSemana = new HashMap<>();
        WeekFields wf = WeekFields.of(Locale.getDefault());

        for (cita c : citasMes) {
            LocalDate d = c.getFechaHora().toLocalDate();
            int semana = d.get(wf.weekOfMonth());
            porSemana.merge(semana, 1L, Long::sum);
        }

        r.setCitasPorSemana(porSemana);

        return r;
    }
}
