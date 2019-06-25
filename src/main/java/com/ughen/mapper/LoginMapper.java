package com.ughen.mapper;

import com.ughen.model.db.ThirdInfo;
import com.ughen.model.db.User;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author feifei
 */
public interface LoginMapper {

		int insert(User user);

		User select(User user);

		/**
		 * 根据id修改单个字段修改 deleteStatus（改为），mobile ，password，mail， username
		 * @param user
		 */
		void update(User user);

		void delete(@Param("id") int id);

		User getUserByThirdUid(Map map);

		ThirdInfo getThird(Map map);

		void addThird(Map map);

		void updateThird(Map map);

}
