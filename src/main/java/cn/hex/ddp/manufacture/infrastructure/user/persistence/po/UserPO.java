package cn.hex.ddp.manufacture.infrastructure.user.persistence.po;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户持久化对象
 *
 * @author Huhaisen
 * @date 2024/04/27
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "user", comment = "用户表")
public class UserPO extends BaseDatabasePO {

    /**
     * 姓名
     */
    @Column(length = 20, comment = "姓名")
    private String name;

    /**
     * 密码
     */
    @Column(length = 88, comment = "密码（加密）")
    private String password;

    /**
     * 手机号
     */
    @Column(length = 11, comment = "手机号")
    @UniqueIndex
    private String phone;

    /**
     * 工号
     */
    @ColumnComment(value = "工号")
    @UniqueIndex
    private String workerNo;

    /**
     * 角色
     */
    @ColumnComment(value = "角色")
    private UserRoleEnum role;

    /**
     * 岗位
     */
    @ColumnComment(value = "岗位")
    @ColumnDefault(value = "0")
    private UserPostEnum post;
}
