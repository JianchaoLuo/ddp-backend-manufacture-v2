//package cn.hex.ddp.manufacture.infrastructure.technique.managerimpl;
//
//import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
//import cn.hex.ddp.manufacture.api.technique.rest.req.SearchMoltenIronFormulaReq;
//import cn.hex.ddp.manufacture.api.technique.rest.req.SearchSandFormulaReq;
//import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
//import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
//import cn.hex.ddp.manufacture.infrastructure.technique.managerimpl.converter.TechniqueInfrastructureConverter;
//import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.MoltenIronFormulaPO;
//import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.SandFormulaPO;
//import cn.hex.ddp.manufacture.infrastructure.technique.persistence.postgresql.repository.MoltenIronFormulaPORepository;
//import cn.hex.ddp.manufacture.infrastructure.technique.persistence.postgresql.repository.SandFormulaPORepository;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * TechniqueManagerImpl#createSandFormula 单元测试
// */
//class TechniqueManagerImplTest {
//
//    @InjectMocks
//    private TechniqueManagerImpl techniqueManager; // 被测类
//
//    @Mock
//    private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//    @Mock
//    private SandFormulaPORepository sandFormulaPORepository; // 模拟持久层
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//    }
//
//    /**
//     * 测试用例 TC001：正常流程
//     * 输入：合法的 SandFormula 对象
//     * 预期：converter 和 repository 各被调用一次
//     */
//    @Test
//    void testCreateSandFormula_Normal() {
//        // 准备测试数据
//        SandFormula sandFormula = new SandFormula();
//        sandFormula.setId(1L); // 添加一些属性以避免潜在的null问题
//        SandFormulaPO sandFormulaPO = new SandFormulaPO();
//        sandFormulaPO.setId(1L);
//
//        // 模拟 converter 行为
//        when(converter.toSandFormulaPO(sandFormula)).thenReturn(sandFormulaPO);
//
//        // 调用被测方法
//        techniqueManager.createSandFormula(sandFormula);
//
//        // 验证 converter 被调用一次
//        verify(converter, times(1)).toSandFormulaPO(sandFormula);
//        // 验证 repository 被调用一次
//        verify(sandFormulaPORepository, times(1)).save(sandFormulaPO);
//    }
//
//    /**
//     * 测试用例 TC002：输入为 null
//     * 输入：null
//     * 预期：方法能正常处理 null 输入，converter 被调用一次
//     */
//    @Test
//    void testCreateSandFormula_NullInput() {
//        // 模拟 converter 处理 null 输入的情况
//        when(converter.toSandFormulaPO(null)).thenReturn(null);
//
//        // 调用方法，不应该抛出异常
//        techniqueManager.createSandFormula(null);
//
//        // 验证 converter 被调用了一次
//        verify(converter, times(1)).toSandFormulaPO(null);
//        // 验证 repository 也被调用了（传入 null）
//        verify(sandFormulaPORepository, times(1)).save(null);
//    }
//
//    /**
//     * TechniqueManagerImpl#deleteSandFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplDeleteSandFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器（虽然本方法不使用，但仍注入）
//
//        @Mock
//        private SandFormulaPORepository sandFormulaPORepository; // 模拟持久层
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程
//         * 输入：有效的 Long id (如 1L)
//         * 预期：repository 的 removeById 被调用一次，并且传参正确
//         */
////        @Test
////        void testDeleteSandFormula_NormalId() {
////            Long id = 1L;
////
////            // 设置 mock 行为：模拟 void 方法调用
////            doNothing().when(sandFormulaPORepository).removeById(id);
////
////            // 执行被测方法
////            techniqueManager.deleteSandFormula(id);
////
////            // 验证 repository 被调用了一次并传递了正确的参数
////            verify(sandFormulaPORepository, times(1)).removeById(id);
////        }
//
//        /**
//         * 测试用例 TC002：输入为 null
//         * 输入：null
//         * 预期：repository 的 removeById 被调用一次，参数为 null
//         */
////        @Test
////        void testDeleteSandFormula_NullId() {
////            Long id = null;
////
////            // 设置 mock 行为：允许传入 null 参数
////            doNothing().when(sandFormulaPORepository).removeById(id);
////
////            // 执行被测方法
////            techniqueManager.deleteSandFormula(id);
////
////            // 验证 repository 被调用了一次并传递了 null 参数
////            verify(sandFormulaPORepository, times(1)).removeById(id);
////        }
//    }
//
//    /**
//     * TechniqueManagerImpl#getSandFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplGetSandFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @Mock
//        private SandFormulaPORepository sandFormulaPORepository; // 模拟持久层
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC1：正常流程 —— 查询成功
//         * 输入：有效 ID (如 1L)
//         * 预期：返回非空 SandFormula 对象
//         */
//        @Test
//        void testGetSandFormula_Normal_Success() {
//            // 准备测试数据
//            Long id = 1L;
//            SandFormulaPO po = new SandFormulaPO();
//            po.setId(id);
//            SandFormula domain = new SandFormula();
//            domain.setId(id);
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getById(id)).thenReturn(po);
//
//            // 模拟 converter 行为
//            when(converter.toSandFormula(po)).thenReturn(domain);
//
//            // 执行方法
//            SandFormula result = techniqueManager.getSandFormula(id);
//
//            // 断言结果
//            assertNotNull(result);
//            assertEquals(domain.getId(), result.getId());
//
//            // 验证调用次数
//            verify(sandFormulaPORepository, times(1)).getById(id);
//            verify(converter, times(1)).toSandFormula(po);
//        }
//
//        /**
//         * 测试用例 TC2：查询不存在的数据
//         * 输入：无效 ID (如 999L)，repository 返回 null
//         * 预期：converter 不被调用，返回 null
//         */
//        @Test
//        void testGetSandFormula_NotFound_ReturnsNull() {
//            // 准备测试数据
//            Long id = 999L;
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getById(id)).thenReturn(null);
//
//            // 执行方法
//            SandFormula result = techniqueManager.getSandFormula(id);
//
//            // 断言结果
//            assertNull(result);
//
//            // 验证调用次数
//            verify(sandFormulaPORepository, times(1)).getById(id);
//            verify(converter, never()).toSandFormula(any(SandFormulaPO.class));
//
//        }
//
//        /**
//         * 测试用例 TC3：输入参数为 null
//         * 输入：null
//         * 预期：repository 接收到 null，converter 不被调用
//         */
//        @Test
//        void testGetSandFormula_NullId_ReturnsNull() {
//            // 准备测试数据
//            Long id = null;
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getById(id)).thenReturn(null);
//
//            // 执行方法
//            SandFormula result = techniqueManager.getSandFormula(id);
//
//            // 断言结果
//            assertNull(result);
//
//            // 验证调用次数
//            verify(sandFormulaPORepository, times(1)).getById(id);
//            verify(converter, never()).toSandFormula(any(SandFormulaPO.class));
//        }
//    }
//
//    /**
//     * TechniqueManagerImpl#getSandFormulaByModel 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplGetSandFormulaByModelTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private SandFormulaPORepository sandFormulaPORepository; // 模拟持久层
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程
//         * 输入：合法的 model 值 "SF001"
//         * 预期：返回 SandFormula 对象
//         */
//        @Test
//        void testGetSandFormulaByModel_Normal() {
//            // 准备测试数据
//            String model = "SF001";
//            SandFormulaPO po = new SandFormulaPO();
//            po.setModel(model);
//            SandFormula domain = new SandFormula();
//            domain.setModel(model);
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(po);
//            // 模拟 converter 行为
//            when(converter.toSandFormula(po)).thenReturn(domain);
//
//            // 调用被测方法
//            SandFormula result = techniqueManager.getSandFormulaByModel(model);
//
//            // 验证结果
//            assertNotNull(result);
//            assertEquals(model, result.getModel());
//            verify(sandFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toSandFormula(po);
//        }
//
//        /**
//         * 测试用例 TC002：输入为 null
//         * 输入：null
//         * 预期：返回 null
//         */
//        @Test
//        void testGetSandFormulaByModel_NullModel() {
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(null);
//            // 模拟 converter 行为
//            verify(converter, times(1)).toSandFormula((SandFormulaPO) null);
//
//            // 调用被测方法
//            SandFormula result = techniqueManager.getSandFormulaByModel(null);
//
//            // 验证结果
//            assertNull(result);
//            verify(sandFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toSandFormula((SandFormulaPO) null);
//        }
//
//        /**
//         * 测试用例 TC003：输入为空字符串
//         * 输入：""
//         * 预期：返回 null
//         */
//        @Test
//        void testGetSandFormulaByModel_EmptyModel() {
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(null);
//            // 模拟 converter 行为
//            when(converter.toSandFormula((SandFormulaPO) null)).thenReturn(null);
//
//            // 调用被测方法
//            SandFormula result = techniqueManager.getSandFormulaByModel("");
//
//            // 验证结果
//            assertNull(result);
//            verify(sandFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toSandFormula((SandFormulaPO) null);
//        }
//
//        /**
//         * 测试用例 TC004：数据库无匹配记录
//         * 输入："SF999"
//         * 预期：返回 null
//         */
//        @Test
//        void testGetSandFormulaByModel_NotFound() {
//            String model = "SF999";
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(null);
//            // 模拟 converter 行为
//            when(converter.toSandFormula((SandFormulaPO) null)).thenReturn(null);
//
//            // 调用被测方法
//            SandFormula result = techniqueManager.getSandFormulaByModel(model);
//
//            // 验证结果
//            assertNull(result);
//            verify(sandFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toSandFormula((SandFormulaPO) isNull());
//
//        }
//    }
//
//    /**
//     * TechniqueManagerImpl#searchSandFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplSearchSandFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private SandFormulaPORepository sandFormulaPORepository; // 模拟持久层
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程，带搜索条件
//         * 输入：SearchSandFormulaReq 包含 search 字段
//         * 预期：返回包含数据的 PageResult
//         */
//        @Test
//        void testSearchSandFormula_WithSearch() {
//            // 准备测试数据
//            SearchSandFormulaReq req = new SearchSandFormulaReq();
//            req.setSearch("test");
//            req.setCurrent(1);
//            req.setPageSize(10);
//
//            Page<SandFormulaPO> page = new Page<>(1, 10);
//            List<SandFormulaPO> poList = Collections.singletonList(new SandFormulaPO());
//            page.setRecords(poList);
//            page.setTotal(1L);
//
//            List<SandFormula> expectedList = Collections.singletonList(new SandFormula());
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.page(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
//
//            // 模拟 converter 行为
//            when(converter.toSandFormula(any(List.class))).thenReturn(expectedList);
//
//            // 调用被测方法
//            PageResult<SandFormula> result = techniqueManager.searchSandFormula(req);
//
//            // 验证结果
//            assertNotNull(result);
//            assertEquals(expectedList.size(), result.getList().size());
//            verify(sandFormulaPORepository, times(1)).page(any(Page.class), any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toSandFormula(any(List.class));
//        }
//
//        /**
//         * 测试用例 TC002：正常流程，无搜索条件
//         * 输入：SearchSandFormulaReq 不包含 search 字段
//         * 预期：返回包含数据的 PageResult
//         */
//        @Test
//        void testSearchSandFormula_WithoutSearch() {
//            // 准备测试数据
//            SearchSandFormulaReq req = new SearchSandFormulaReq();
//            req.setSearch(null);
//            req.setCurrent(1);
//            req.setPageSize(10);
//
//            Page<SandFormulaPO> page = new Page<>(1, 10);
//            List<SandFormulaPO> poList = Collections.singletonList(new SandFormulaPO());
//            page.setRecords(poList);
//            page.setTotal(1L);
//
//            List<SandFormula> expectedList = Collections.singletonList(new SandFormula());
//            PageResult<SandFormula> expectedResult = PageResult.of(1, 1, 10, expectedList);
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.page(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
//
//            // 模拟 converter 行为
//            when(converter.toSandFormula(any(List.class))).thenReturn(expectedList);
//
//            // 调用被测方法
//            PageResult<SandFormula> result = techniqueManager.searchSandFormula(req);
//
//            // 验证结果
//            assertNotNull(result);
//            // 验证总记录数
//            assertEquals(expectedResult.getTotal(), result.getTotal());
//            // 验证上一页页码
//            assertEquals(expectedResult.getPrev(), result.getPrev());
//            // 验证下一页页码
//            assertEquals(expectedResult.getNext(), result.getNext());
//            // 验证数据列表大小
//            assertEquals(expectedResult.getList().size(), result.getList().size());
//
//            verify(sandFormulaPORepository, times(1)).page(any(Page.class), any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toSandFormula(any(List.class));
//        }
//
//        /**
//         * 测试用例 TC003：输入为 null
//         * 输入：null
//         * 预期：抛出 NullPointerException
//         */
//        @Test
//        void testSearchSandFormula_NullInput() {
//            assertThrows(NullPointerException.class, () -> {
//                techniqueManager.searchSandFormula(null);
//            });
//        }
//
//        /**
//         * 测试用例 TC004：分页参数不合法
//         * 输入：current 为负数
//         * 预期：抛出异常或返回空结果（根据实际业务逻辑）
//         */
//        @Test
//        void testSearchSandFormula_IllegalPageParams() {
//            // 准备测试数据
//            SearchSandFormulaReq req = new SearchSandFormulaReq();
//            req.setCurrent(-1); // 不合法的 current
//            req.setPageSize(10);
//
//            // 模拟 repository 行为
//            when(sandFormulaPORepository.page(any(Page.class), any(LambdaQueryWrapper.class))).thenThrow(new IllegalArgumentException("页码不合法"));
//
//            // 验证抛出异常
//            assertThrows(IllegalArgumentException.class, () -> {
//                techniqueManager.searchSandFormula(req);
//            });
//        }
//    }
//
//    /**
//     * TechniqueManagerImpl#updateSandFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplUpdateSandFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @Mock
//        private SandFormulaPORepository sandFormulaPORepository; // 模拟持久层
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程
//         * 输入：合法的 SandFormula 对象
//         * 预期：converter 和 repository 各被调用一次
//         */
////        @Test
////        void testUpdateSandFormula_Normal() {
////            // 准备测试数据
////            SandFormula sandFormula = new SandFormula();
////            sandFormula.setId(1L);
////            sandFormula.setModel("SF-001");
////            sandFormula.setName("砂配方一号");
////
////            SandFormulaPO sandFormulaPO = new SandFormulaPO();
////            sandFormulaPO.setId(1L);
////            sandFormulaPO.setModel("SF-001");
////            sandFormulaPO.setName("砂配方一号");
////
////            // 模拟 converter 行为
////            when(converter.toSandFormulaPO(sandFormula)).thenReturn(sandFormulaPO);
////
////            // 模拟 repository 行为
////            doNothing().when(sandFormulaPORepository).updateById(sandFormulaPO);
////
////            // 调用被测方法
////            techniqueManager.updateSandFormula(sandFormula);
////
////            // 验证 converter 被调用一次
////            verify(converter, times(1)).toSandFormulaPO(sandFormula);
////            // 验证 repository 被调用一次
////            verify(sandFormulaPORepository, times(1)).updateById(sandFormulaPO);
////        }
//
//        /**
//         * 测试用例 TC002：输入为 null
//         * 输入：null
//         * 预期：converter 被调用一次，repository 也被调用一次（传入 null）
//         */
////        @Test
////        void testUpdateSandFormula_NullInput() {
////            // 模拟 converter 处理 null 输入的情况
////            when(converter.toSandFormulaPO(null)).thenReturn(null);
////
////            // 模拟 repository 行为
////            doNothing().when(sandFormulaPORepository).updateById(null);
////
////            // 调用方法，不应该抛出异常
////            techniqueManager.updateSandFormula(null);
////
////            // 验证 converter 被调用了一次
////            verify(converter, times(1)).toSandFormulaPO(null);
////            // 验证 repository 也被调用了（传入 null）
////            verify(sandFormulaPORepository, times(1)).updateById(null);
////        }
//    }
//
//    /**
//     * TechniqueManagerImpl#createMoltenIronFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplCreateMoltenIronFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @Mock
//        private MoltenIronFormulaPORepository moltenIronFormulaPORepository; // 模拟持久层
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程
//         * 输入：合法的 MoltenIronFormula 对象
//         * 预期：converter 和 repository 各被调用一次
//         */
////        @Test
////        void testCreateMoltenIronFormula_Normal() {
////            // 准备测试数据
////            MoltenIronFormula moltenIronFormula = new MoltenIronFormula();
////            moltenIronFormula.setId(1L);
////            MoltenIronFormulaPO moltenIronFormulaPO = new MoltenIronFormulaPO();
////            moltenIronFormulaPO.setId(1L);
////
////            // 模拟 converter 行为
////            when(converter.toMoltenIronFormulaPO(moltenIronFormula)).thenReturn(moltenIronFormulaPO);
////
////            // 模拟 repository 行为（void 方法）
////            doNothing().when(moltenIronFormulaPORepository).save(moltenIronFormulaPO);
////
////            // 调用被测方法
////            techniqueManager.createMoltenIronFormula(moltenIronFormula);
////
////            // 验证 converter 被调用一次
////            verify(converter, times(1)).toMoltenIronFormulaPO(moltenIronFormula);
////            // 验证 repository 被调用一次
////            verify(moltenIronFormulaPORepository, times(1)).save(moltenIronFormulaPO);
////        }
//
//        /**
//         * 测试用例 TC002：输入为 null
//         * 输入：null
//         * 预期：方法能正常处理 null 输入，converter 被调用一次
//         */
////        @Test
////        void testCreateMoltenIronFormula_NullInput() {
////            // 模拟 converter 处理 null 输入的情况
////            when(converter.toMoltenIronFormulaPO(null)).thenReturn(null);
////
////            // 模拟 repository 接受 null 参数的行为
////            doNothing().when(moltenIronFormulaPORepository).save(null);
////
////            // 调用方法，不应该抛出异常
////            techniqueManager.createMoltenIronFormula(null);
////
////            // 验证 converter 被调用了一次
////            verify(converter, times(1)).toMoltenIronFormulaPO(null);
////            // 验证 repository 也被调用了（传入 null）
////            verify(moltenIronFormulaPORepository, times(1)).save(null);
////        }
//    }
//
//    /**
//     * TechniqueManagerImpl#deleteMoltenIronFormula 单元测试
//     */
////    @Nested
////    class TechniqueManagerImplDeleteMoltenIronFormulaTest {
////
////        @InjectMocks
////        private TechniqueManagerImpl techniqueManager; // 被测类
////
////        @Mock
////        private MoltenIronFormulaPORepository moltenIronFormulaPORepository; // 模拟持久层
////
////        @Mock
////        private TechniqueInfrastructureConverter converter; // 模拟转换器（虽然未使用但保留注入）
////
////        @BeforeEach
////        void setUp() {
////            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
////        }
////
////        /**
////         * 测试用例 TC001：正常流程
////         * 输入：有效的 Long id (如 1L)
////         * 预期：repository 的 removeById 被调用一次，并且传参正确
////         */
////        @Test
////        void testDeleteMoltenIronFormula_NormalId() {
////            Long id = 1L;
////
////            // 设置 mock 行为：模拟 void 方法调用
////            doNothing().when(moltenIronFormulaPORepository).removeById(id);
////
////            // 执行被测方法
////            techniqueManager.deleteMoltenIronFormula(id);
////
////            // 验证 repository 被调用了一次并传递了正确的参数
////            verify(moltenIronFormulaPORepository, times(1)).removeById(id);
////        }
////
////        /**
////         * 测试用例 TC002：输入为 null
////         * 输入：null
////         * 预期：repository 的 removeById 被调用一次，参数为 null
////         */
////        @Test
////        void testDeleteMoltenIronFormula_NullId() {
////            Long id = null;
////
////            // 设置 mock 行为：允许传入 null 参数
////            doNothing().when(moltenIronFormulaPORepository).removeById(id);
////
////            // 执行被测方法
////            techniqueManager.deleteMoltenIronFormula(id);
////
////            // 验证 repository 被调用了一次并传递了 null 参数
////            verify(moltenIronFormulaPORepository, times(1)).removeById(id);
////        }
////    }
//
//    /**
//     * TechniqueManagerImpl#getMoltenIronFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplGetMoltenIronFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private MoltenIronFormulaPORepository moltenIronFormulaPORepository; // 模拟持久层
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程
//         * 输入：合法存在的 Long id
//         * 预期：repository 被调用一次，converter 被调用一次并返回转换后的对象
//         */
//        @Test
//        void testGetMoltenIronFormula_NormalId_ReturnsConvertedObject() {
//            // 准备测试数据
//            Long id = 1L;
//            MoltenIronFormulaPO po = new MoltenIronFormulaPO();
//            po.setId(id);
//            MoltenIronFormula domain = new MoltenIronFormula();
//            domain.setId(id);
//
//            // 模拟 repository 行为
//            when(moltenIronFormulaPORepository.getById(id)).thenReturn(po);
//
//            // 模拟 converter 行为
//            when(converter.toMoltenIronFormula(po)).thenReturn(domain);
//
//            // 执行被测方法
//            MoltenIronFormula result = techniqueManager.getMoltenIronFormula(id);
//
//            // 断言结果
//            assertNotNull(result);
//            assertEquals(domain.getId(), result.getId());
//
//            // 验证调用次数
//            verify(moltenIronFormulaPORepository, times(1)).getById(id);
//            verify(converter, times(1)).toMoltenIronFormula(po);
//        }
//
//        /**
//         * 测试用例 TC002：输入为 null
//         * 输入：null
//         * 预期：repository 被调用一次传入 null，converter 被调用一次传入 null，最终返回 null
//         */
//        @Test
//        void testGetMoltenIronFormula_NullId_ReturnsNull() {
//            // 模拟 repository 行为
//            when(moltenIronFormulaPORepository.getById(null)).thenReturn(null);
//
//            // 模拟 converter 行为
//            when(converter.toMoltenIronFormula((MoltenIronFormulaPO) null)).thenReturn(null);
//
//            // 执行被测方法
//            MoltenIronFormula result = techniqueManager.getMoltenIronFormula(null);
//
//            // 断言结果
//            assertNull(result);
//
//            // 验证调用次数
//            verify(moltenIronFormulaPORepository, times(1)).getById(null);
//            verify(converter, times(1)).toMoltenIronFormula((MoltenIronFormulaPO) null);
//        }
//
//        /**
//         * 测试用例 TC003：根据 id 查不到数据
//         * 输入：不存在的 Long id
//         * 预期：repository 返回 null，converter 被调用一次传入 null，最终返回 null
//         */
//        @Test
//        void testGetMoltenIronFormula_IdNotFound_ReturnsNull() {
//            // 准备测试数据
//            Long id = 999L;
//
//            // 模拟 repository 行为
//            when(moltenIronFormulaPORepository.getById(id)).thenReturn(null);
//
//            // 模拟 converter 行为
//            when(converter.toMoltenIronFormula((MoltenIronFormulaPO) null)).thenReturn(null);
//
//            // 执行被测方法
//            MoltenIronFormula result = techniqueManager.getMoltenIronFormula(id);
//
//            // 断言结果
//            assertNull(result);
//
//            // 验证调用次数
//            verify(moltenIronFormulaPORepository, times(1)).getById(id);
//            verify(converter, times(1)).toMoltenIronFormula((MoltenIronFormulaPO) null);
//        }
//    }
//
//    /**
//     * TechniqueManagerImpl#getMoltenIronFormulaByModel 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplGetMoltenIronFormulaByModelTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private MoltenIronFormulaPORepository moltenIronFormulaPORepository; // 模拟持久层
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程 - 存在对应 model 的记录
//         * 输入：合法的 model 字符串 "ABC"
//         * 预期：成功获取并转换返回 MoltenIronFormula 实体
//         */
//        @Test
//        void testGetMoltenIronFormulaByModel_Normal() {
//            // 准备测试数据
//            String model = "ABC";
//            MoltenIronFormulaPO po = new MoltenIronFormulaPO();
//            po.setModel(model);
//
//            MoltenIronFormula domainModel = new MoltenIronFormula();
//            domainModel.setModel(model);
//
//            // 模拟 repository 行为
//            when(moltenIronFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(po);
//
//            // 模拟 converter 行为
//            when(converter.toMoltenIronFormula(po)).thenReturn(domainModel);
//
//            // 执行被测方法
//            MoltenIronFormula result = techniqueManager.getMoltenIronFormulaByModel(model);
//
//            // 断言结果
//            assertNotNull(result);
//            assertEquals(model, result.getModel());
//
//            // 验证调用次数
//            verify(moltenIronFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, times(1)).toMoltenIronFormula(po);
//        }
//
//        /**
//         * 测试用例 TC002：查询结果为空
//         * 输入："XYZ"（不存在）
//         * 预期：返回 null
//         */
//        @Test
//        void testGetMoltenIronFormulaByModel_NotFound() {
//            String model = "XYZ";
//
//            // 模拟 repository 返回 null
//            when(moltenIronFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(null);
//
//            // 执行被测方法
//            MoltenIronFormula result = techniqueManager.getMoltenIronFormulaByModel(model);
//
//            // 断言结果
//            assertNull(result);
//
//            // 验证调用次数
//            verify(moltenIronFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, never()).toMoltenIronFormula(any(MoltenIronFormulaPO.class));
//        }
//
//        /**
//         * 测试用例 TC003：model 为 null
//         * 输入：null
//         * 预期：查询条件允许 null，repository 应接收 null 查询
//         */
//        @Test
//        void testGetMoltenIronFormulaByModel_NullModel() {
//            // 模拟 repository 接收 null 查询
//            when(moltenIronFormulaPORepository.getOne(any(LambdaQueryWrapper.class))).thenReturn(null);
//
//            // 执行被测方法
//            MoltenIronFormula result = techniqueManager.getMoltenIronFormulaByModel(null);
//
//            // 断言结果
//            assertNull(result);
//
//            // 验证调用次数
//            verify(moltenIronFormulaPORepository, times(1)).getOne(any(LambdaQueryWrapper.class));
//            verify(converter, never()).toMoltenIronFormula(isNull(MoltenIronFormulaPO.class));
//        }
//    }
//
//    /**
//     * TechniqueManagerImpl#searchMoltenIronFormula 单元测试
//     */
//    @Nested
//    class TechniqueManagerImplSearchMoltenIronFormulaTest {
//
//        @InjectMocks
//        private TechniqueManagerImpl techniqueManager; // 被测类
//
//        @Mock
//        private MoltenIronFormulaPORepository moltenIronFormulaPORepository; // 模拟持久层
//
//        @Mock
//        private TechniqueInfrastructureConverter converter; // 模拟转换器
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
//        }
//
//        /**
//         * 测试用例 TC001：正常流程，带搜索关键字
//         * 输入：SearchMoltenIronFormulaReq 包含有效 search 关键字
//         * 预期：repository.page 被调用一次；converter 被调用一次
//         */
////        @Test
////        void testSearchMoltenIronFormula_WithSearchKeyword() {
////            // 准备请求参数
////            SearchMoltenIronFormulaReq req = new SearchMoltenIronFormulaReq();
////            req.setSearch("test");
////            req.setCurrent(1);
////            req.setPageSize(10);
////
////            // 构造模拟数据
////            MoltenIronFormulaPO po1 = new MoltenIronFormulaPO();
////            po1.setId(1L);
////            po1.setName("testName");
////
////            Page<MoltenIronFormulaPO> page = new Page<>(1, 10);
////            page.setRecords(Collections.singletonList(po1));
////            page.setTotal(1L);
////
////            MoltenIronFormula domain = new MoltenIronFormula();
////            domain.setId(1L);
////            domain.setName("testName");
////
////            // 模拟 repository.page 返回值
////            when(moltenIronFormulaPORepository.page(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
////
////            // 模拟 converter 转换行为
////            when(converter.toMoltenIronFormula(any(MoltenIronFormulaPO.class))).thenAnswer(invocation -> {
////                MoltenIronFormulaPO inputPO = invocation.getArgument(0);
////                // 实现转换逻辑
////                return new MoltenIronFormula(); // 返回转换后的对象
////            });
////
////            // 调用被测方法
////            PageResult<MoltenIronFormula> result = techniqueManager.searchMoltenIronFormula(req);
////
////            // 断言结果
////            assertNotNull(result);
////            assertEquals(1, result.getList().size());
////            assertEquals(1, result.getTotal());
////
////            // 验证 repository 被调用一次
////            verify(moltenIronFormulaPORepository, times(1)).page(any(Page.class), any(LambdaQueryWrapper.class));
////
////            // 验证 converter 被调用一次
////            verify(converter, times(1)).toMoltenIronFormula(any(MoltenIronFormulaPO.class));
////
////        }
//
//        /**
//         * 测试用例 TC002：搜索关键字为空
//         * 输入：SearchMoltenIronFormulaReq 中 search 为空字符串
//         * 预期：repository.page 被调用一次；converter 被调用一次
//         */
//        @Test
//        void testSearchMoltenIronFormula_EmptySearchKeyword() {
//            // 准备请求参数
//            SearchMoltenIronFormulaReq req = new SearchMoltenIronFormulaReq();
//            req.setSearch("");
//            req.setCurrent(1);
//            req.setPageSize(10);
//
//            // 构造模拟数据
//            MoltenIronFormulaPO po1 = new MoltenIronFormulaPO();
//            po1.setId(1L);
//            po1.setName("defaultName");
//
//            Page<MoltenIronFormulaPO> page = new Page<>(1, 10);
//            page.setRecords(Collections.singletonList(po1));
//            page.setTotal(1L);
//
//            MoltenIronFormula domain = new MoltenIronFormula();
//            domain.setId(1L);
//            domain.setName("defaultName");
//
//            // 模拟 repository.page 返回值
//            when(moltenIronFormulaPORepository.page(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
//
//            // 模拟 converter 转换行为
//            when(converter.toMoltenIronFormula(anyList())).thenAnswer(invocation -> {
//                List<MoltenIronFormulaPO> inputList = invocation.getArgument(0);
//                // 实现实际的转换逻辑
//                return inputList.stream()
//                        .map(po -> {
//                            MoltenIronFormula formula = new MoltenIronFormula();
//                            formula.setId(po.getId());
//                            formula.setName(po.getName());
//                            return formula;
//                        })
//                        .collect(Collectors.toList());
//            });
//
//            // 调用被测方法
//            PageResult<MoltenIronFormula> result = techniqueManager.searchMoltenIronFormula(req);
//
//            // 断言结果
//            assertNotNull(result);
//            assertEquals(1, result.getList().size());
//            assertEquals(1, result.getTotal());
//
//            // 验证 repository 被调用一次
//            verify(moltenIronFormulaPORepository, times(1)).page(any(Page.class), any(LambdaQueryWrapper.class));
//
//            // 验证方法签名
//            verify(converter, times(1)).toMoltenIronFormula(anyList());
//        }
//
//        /**
//         * 测试用例 TC003：请求参数为 null
//         * 输入：null
//         * 预期：抛出 NullPointerException（因为当前方法未做 null 判定）
//         */
//        @Test
//        void testSearchMoltenIronFormula_NullRequest() {
//            assertThrows(NullPointerException.class, () -> {
//                techniqueManager.searchMoltenIronFormula(null);
//            });
//        }
//    }
//}
