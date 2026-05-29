package com.sunghyun.batch.infrastructure.typehandler;

import com.sunghyun.plab.subscription.domain.enums.NotiSetting;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotiSettingTypeHandler extends BaseTypeHandler<NotiSetting> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, NotiSetting parameter, JdbcType jdbcType) throws SQLException {
        // DB에 저장할 때: Enum의 code값("6", "SUPER_SUB" 등)을 저장
        ps.setString(i, parameter.getCode());
    }

    @Override
    public NotiSetting getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // DB에서 읽어올 때 (컬럼명 기준): code값을 NotiSetting으로 변환
        String code = rs.getString(columnName);
        return code == null ? null : NotiSetting.fromCode(code);
    }

    @Override
    public NotiSetting getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return code == null ? null : NotiSetting.fromCode(code);
    }

    @Override
    public NotiSetting getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return code == null ? null : NotiSetting.fromCode(code);
    }
}