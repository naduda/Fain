package jdbc;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import model.DvalTI;
import model.DvalTS;
import model.Tsignal;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface IMapper {
//	@Select("SELECT idsignal, * FROM t_signal where status = 1 and typesignalref in (1, 2) and namesignal not like '%Связь с у%' order by namesignal")
	@Select("SELECT idsignal, * FROM t_signal")
	@MapKey("idsignal")
	Map<Integer, Tsignal> getTsignalsMap();
	
//	@Select("SELECT * FROM t_signal where status = 1 and typesignalref in (1, 2) and namesignal not like '%Связь с у%' order by namesignal")
	@Select("SELECT * FROM t_signal")
	List<Tsignal> getTsignals();
	
	@Select("SELECT * FROM t_signal WHERE idsignal = #{id}")
	Tsignal getTsignalById(int id);
	
	@Select("select * from d_valti where servdt > #{servdt} order by servdt desc")
	List<DvalTI> getLastTI(Timestamp servdt);

	@Select("select * from d_valts where servdt > #{servdt} order by servdt desc")
	List<DvalTS> getLastTS(Timestamp servdt);
	
	@Select("select set_ts(#{idsig}, #{val_p}, now()::timestamp without time zone, 107, -1, #{schemeref})")
	Integer setTS(@Param("idsig") int idsignal, @Param("val_p") double val, @Param("schemeref") int schemeref);
}
