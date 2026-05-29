package com.sunghyun.batch.infrastructure.typehandler;

import com.sunghyun.plab.match.domain.enums.MatchStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MatchStatusTypeHandler extends BaseTypeHandler<MatchStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MatchStatus parameter, JdbcType jdbcType) throws SQLException {
        // Enum -> DB (코드값 저장)
        ps.setString(i, parameter.getCode());
    }

    @Override
    public MatchStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return MatchStatus.fromCode(rs.getString(columnName)); // DB 코드값 -> Enum
    }

    @Override
    public MatchStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return MatchStatus.fromCode(rs.getString(columnIndex));
    }

    @Override
    public MatchStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return MatchStatus.fromCode(cs.getString(columnIndex));
    }}
