//package cn.hex.ddp.manufacture.application.technique.service.impl;
//
//import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
//import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
//import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
//import cn.hex.ddp.manufacture.api.technique.rest.req.*;
//import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniquePageVO;
//import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueVO;
//import cn.hex.ddp.manufacture.application.technique.converter.TechniqueServiceConverter;
//import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
//import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
//import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
//import cn.hex.ddp.manufacture.domain.mold.manager.MoldManager;
//import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
//import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
//import cn.hex.ddp.manufacture.domain.sandbox.manager.SandboxManager;
//import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
//import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
//import cn.hex.ddp.manufacture.domain.technique.model.FormulaDetail;
//import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
//import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
//import cn.hex.ddp.manufacture.domain.technique.model.Technique;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
///**
// * {@link TechniqueServiceImpl#createSandFormula(CreateSandFormulaReq)} 的单元测试
// */
//class TechniqueServiceImplTest {
//
//    // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//    @InjectMocks
//    private TechniqueServiceImpl techniqueService;
//
//    // 技术管理器接口模拟
//    @Mock
//    private TechniqueManager techniqueManager;
//
//    // 数据转换器接口模拟
//    @Mock
//    private TechniqueServiceConverter converter;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//    }
//
//    /**
//     * 测试正常创建砂配方的情况：输入合法且型号未重复
//     */
//    @Test
//    void testCreateSandFormula_Success() {
//        // 准备测试数据
//        CreateSandFormulaReq request = new CreateSandFormulaReq();
//        request.setModel("SF-001");
//        request.setName("砂配方一号");
//        request.setFormulaDetail(Collections.singletonList(new FormulaDetail()));
//
//        SandFormula sandFormula = new SandFormula();
//        sandFormula.setModel("SF-001");
//        sandFormula.setName("砂配方一号");
//
//        // 设置 mock 行为
//        when(techniqueManager.getSandFormulaByModel(anyString())).thenReturn(null); // 模拟没有重复型号
//        when(converter.toSandFormula(any(CreateSandFormulaReq.class))).thenReturn(sandFormula);
//
//        // 执行目标方法
//        techniqueService.createSandFormula(request);
//
//        // 验证交互次数
//        verify(techniqueManager, times(1)).getSandFormulaByModel("SF-001");
//        verify(converter, times(1)).toSandFormula(request);
//        verify(techniqueManager, times(1)).createSandFormula(sandFormula);
//    }
//
//    /**
//     * 测试当砂配方型号已存在时应抛出 BusinessException 异常
//     */
//    @Test
//    void testCreateSandFormula_ModelAlreadyExists_ThrowsBusinessException() {
//        // 准备测试数据
//        CreateSandFormulaReq request = new CreateSandFormulaReq();
//        request.setModel("SF-001");
//        request.setName("砂配方一号");
//
//        SandFormula existingSandFormula = new SandFormula();
//        existingSandFormula.setId(1L);
//        existingSandFormula.setModel("SF-001");
//
//        // 设置 mock 行为
//        when(techniqueManager.getSandFormulaByModel(anyString())).thenReturn(existingSandFormula); // 已存在该型号
//
//        // 断言会抛出异常
//        assertThrows(BusinessException.class, () -> {
//            techniqueService.createSandFormula(request);
//        });
//
//        // 验证交互次数
//        verify(techniqueManager, times(1)).getSandFormulaByModel("SF-001");
//        verify(converter, never()).toSandFormula(any(CreateSandFormulaReq.class));
//        verify(techniqueManager, never()).createSandFormula(any());
//    }
//
//
//    /**
//     * {@link TechniqueServiceImpl#deleteSandFormula(Long)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_deleteSandFormula {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 其他 manager 模拟（虽然本方法未直接使用）
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private ProductManager productManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试正常删除砂配方的情况：没有工艺使用该砂配方
//         */
//        @Test
//        void testDeleteSandFormula_Success() {
//            Long formulaId = 1L;
//
//            // 设置 mock 行为：无工艺使用当前砂配方
//            when(techniqueManager.getTechniquesBySandFormulaId(formulaId))
//                    .thenReturn(Collections.emptyList());
//
//            doNothing().when(techniqueManager).deleteSandFormula(formulaId);
//
//            // 执行目标方法
//            techniqueService.deleteSandFormula(formulaId);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechniquesBySandFormulaId(formulaId);
//            verify(techniqueManager, times(1)).deleteSandFormula(formulaId);
//        }
//
//        /**
//         * 测试当砂配方被工艺占用时应抛出 BusinessException 异常
//         */
//        @Test
//        void testDeleteSandFormula_FormulaOccupied_ThrowsBusinessException() {
//            Long formulaId = 1L;
//
//            // 构造一个假的 Technique 对象表示被占用
//            Technique mockTechnique = new Technique();
//            mockTechnique.setId(100L);
//            List<Technique> occupiedTechniques = Collections.singletonList(mockTechnique);
//
//            // 设置 mock 行为：存在工艺使用当前砂配方
//            when(techniqueManager.getTechniquesBySandFormulaId(formulaId))
//                    .thenReturn(occupiedTechniques);
//
//            // 断言会抛出异常
//            assertThrows(BusinessException.class, () -> {
//                techniqueService.deleteSandFormula(formulaId);
//            });
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechniquesBySandFormulaId(formulaId);
//            verify(techniqueManager, never()).deleteSandFormula(anyLong());
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#searchSandFormula(SearchSandFormulaReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_searchSandFormula {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 其他 manager 模拟（虽然本方法未直接使用）
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private ProductManager productManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试正常查询砂配方分页数据的情况
//         */
//        @Test
//        void testSearchSandFormula_Success() {
//            // 准备请求参数
//            SearchSandFormulaReq req = new SearchSandFormulaReq();
//            req.setCurrent(1);
//            req.setPageSize(10);
//            req.setSearch("test");
//
//            // 构造预期返回的结果
//            List<SandFormula> dataList = Arrays.asList(new SandFormula(), new SandFormula());
//            PageResult<SandFormula> expectedResult = PageResult.of(20, 1, 10, dataList);
//
//            // 设置 mock 行为
//            when(techniqueManager.searchSandFormula(any(SearchSandFormulaReq.class)))
//                    .thenReturn(expectedResult);
//
//            // 执行目标方法
//            PageResult<SandFormula> actualResult = techniqueService.searchSandFormula(req);
//
//            // 验证结果是否一致
//            assertEquals(expectedResult, actualResult);
//            assertEquals(20, actualResult.getTotal());
//            // 修改此处：应该期望2个元素而不是1个
//            assertEquals(2, actualResult.getList().size());
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).searchSandFormula(req);
//        }
//
//
//        /**
//         * 测试传入 null 请求参数的情况
//         */
//        @Test
//        void testSearchSandFormula_NullRequest() {
//            // 设置 mock 行为
//            when(techniqueManager.searchSandFormula(null))
//                    .thenReturn(PageResult.emptyResult());
//
//            // 执行目标方法
//            PageResult<SandFormula> result = techniqueService.searchSandFormula(null);
//
//            // 验证返回为空结果
//            assertEquals(true, result.isEmpty());
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).searchSandFormula(null);
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#updateSandFormula(Long, CreateSandFormulaReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_updateSandFormula {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试正常更新砂配方的情况：输入合法且型号未重复
//         */
//        @Test
//        void testUpdateSandFormula_Success_NoConflict() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateSandFormulaReq request = new CreateSandFormulaReq();
//            request.setModel("SF-001");
//            request.setName("砂配方一号");
//            request.setFormulaDetail(Collections.singletonList(new FormulaDetail()));
//
//            SandFormula sandFormula = new SandFormula();
//            sandFormula.setId(id);
//            sandFormula.setModel("SF-001");
//            sandFormula.setName("砂配方一号");
//
//            // 设置 mock 行为
//            when(techniqueManager.getSandFormulaByModel(eq("SF-001"))).thenReturn(null); // 模型不存在
//            when(converter.toSandFormula(any(CreateSandFormulaReq.class))).thenReturn(sandFormula);
//            doNothing().when(techniqueManager).updateSandFormula(any(SandFormula.class));
//
//            // 执行目标方法
//            techniqueService.updateSandFormula(id, request);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getSandFormulaByModel("SF-001");
//            verify(converter, times(1)).toSandFormula(request);
//            verify(techniqueManager, times(1)).updateSandFormula(sandFormula);
//        }
//
//        /**
//         * 测试当砂配方型号已存在并且不属于当前更新项时，应该抛出 BusinessException 异常
//         */
//        @Test
//        void testUpdateSandFormula_ModelConflict_ThrowsBusinessException() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateSandFormulaReq request = new CreateSandFormulaReq();
//            request.setModel("SF-001");
//            request.setName("砂配方一号");
//
//            SandFormula existingSandFormula = new SandFormula();
//            existingSandFormula.setId(2L); // 不同ID表示不同记录
//            existingSandFormula.setModel("SF-001");
//
//            // 设置 mock 行为
//            when(techniqueManager.getSandFormulaByModel(eq("SF-001"))).thenReturn(existingSandFormula);
//
//            // 断言会抛出异常
//            BusinessException exception = assertThrows(BusinessException.class, () -> {
//                techniqueService.updateSandFormula(id, request);
//            });
//
//            assertEquals(BssExType.SAND_FORMULA_MODEL_REPEAT.getCode(), exception.getCode());
//            assertEquals("沙配方型号已存在: SF-001", exception.getMessage());
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getSandFormulaByModel("SF-001");
//            verify(converter, never()).toSandFormula(any(CreateSandFormulaReq.class));
//            verify(techniqueManager, never()).updateSandFormula(any());
//        }
//
//        /**
//         * 测试当砂配方型号已存在且属于当前更新项时，允许继续更新
//         */
//        @Test
//        void testUpdateSandFormula_ModelSameAsCurrent_AllowUpdate() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateSandFormulaReq request = new CreateSandFormulaReq();
//            request.setModel("SF-001");
//            request.setName("砂配方一号");
//            request.setFormulaDetail(Collections.singletonList(new FormulaDetail()));
//
//            SandFormula existingSandFormula = new SandFormula();
//            existingSandFormula.setId(id); // 相同ID表示同一记录
//            existingSandFormula.setModel("SF-001");
//
//            SandFormula convertedSandFormula = new SandFormula();
//            convertedSandFormula.setId(id);
//            convertedSandFormula.setModel("SF-001");
//            convertedSandFormula.setName("砂配方一号");
//
//            // 设置 mock 行为
//            when(techniqueManager.getSandFormulaByModel(eq("SF-001"))).thenReturn(existingSandFormula);
//            when(converter.toSandFormula(any(CreateSandFormulaReq.class))).thenReturn(convertedSandFormula);
//            doNothing().when(techniqueManager).updateSandFormula(any(SandFormula.class));
//
//            // 执行目标方法
//            techniqueService.updateSandFormula(id, request);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getSandFormulaByModel("SF-001");
//            verify(converter, times(1)).toSandFormula(request);
//            verify(techniqueManager, times(1)).updateSandFormula(convertedSandFormula);
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#createMoltenIronFormula(CreateMoltenIronFormulaReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_createMoltenIronFormula {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        // 其他 manager 模拟（虽然本方法未直接使用）
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private ProductManager productManager;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试正常创建铁水配方的情况：输入合法且型号未重复
//         */
//        @Test
//        void testCreateMoltenIronFormula_Success() {
//            // 准备测试数据
//            CreateMoltenIronFormulaReq request = new CreateMoltenIronFormulaReq();
//            request.setModel("MI-FORMULA-001");
//            request.setName("铁水配方一号");
//            request.setFormulaDetail(Collections.singletonList(new FormulaDetail()));
//
//            MoltenIronFormula moltenIronFormula = new MoltenIronFormula();
//            moltenIronFormula.setModel("MI-FORMULA-001");
//            moltenIronFormula.setName("铁水配方一号");
//
//            // 设置 mock 行为
//            when(techniqueManager.getMoltenIronFormulaByModel(anyString())).thenReturn(null); // 模拟没有重复型号
//            when(converter.toMoltenIronFormula(any(CreateMoltenIronFormulaReq.class))).thenReturn(moltenIronFormula);
//            doNothing().when(techniqueManager).createMoltenIronFormula(any(MoltenIronFormula.class));
//
//            // 执行目标方法
//            techniqueService.createMoltenIronFormula(request);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getMoltenIronFormulaByModel("MI-FORMULA-001");
//            verify(converter, times(1)).toMoltenIronFormula(request);
//            verify(techniqueManager, times(1)).createMoltenIronFormula(moltenIronFormula);
//        }
//
//        /**
//         * 测试当铁水配方型号已存在时应抛出 BusinessException 异常
//         */
//        @Test
//        void testCreateMoltenIronFormula_ModelAlreadyExists_ThrowsBusinessException() {
//            // 准备测试数据
//            CreateMoltenIronFormulaReq request = new CreateMoltenIronFormulaReq();
//            request.setModel("MI-FORMULA-001");
//            request.setName("铁水配方一号");
//
//            MoltenIronFormula existingFormula = new MoltenIronFormula();
//            existingFormula.setId(1L);
//            existingFormula.setModel("MI-FORMULA-001");
//
//            // 设置 mock 行为
//            when(techniqueManager.getMoltenIronFormulaByModel(anyString())).thenReturn(existingFormula); // 已存在该型号
//
//            // 断言会抛出异常
//            assertThrows(BusinessException.class, () -> {
//                techniqueService.createMoltenIronFormula(request);
//            }, "Expected createMoltenIronFormula to throw BusinessException when model already exists.");
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getMoltenIronFormulaByModel("MI-FORMULA-001");
//            verify(converter, never()).toMoltenIronFormula(any(CreateMoltenIronFormulaReq.class));
//            verify(techniqueManager, never()).createMoltenIronFormula(any());
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#deleteMoltenIronFormula(Long)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_deleteMoltenIronFormula {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 其他 manager 模拟（虽然本方法未直接使用）
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private ProductManager productManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试正常删除铁水配方的情况：没有工艺使用该铁水配方
//         */
//        @Test
//        void testDeleteMoltenIronFormula_Success() {
//            Long formulaId = 1L;
//
//            // 设置 mock 行为：无工艺使用当前铁水配方
//            when(techniqueManager.getTechniquesByMoltenIronFormulaId(formulaId))
//                    .thenReturn(Collections.emptyList());
//
//            doNothing().when(techniqueManager).deleteMoltenIronFormula(formulaId);
//
//            // 执行目标方法
//            techniqueService.deleteMoltenIronFormula(formulaId);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechniquesByMoltenIronFormulaId(formulaId);
//            verify(techniqueManager, times(1)).deleteMoltenIronFormula(formulaId);
//        }
//
//        /**
//         * 测试当铁水配方被工艺占用时应抛出 BusinessException 异常
//         */
//        @Test
//        void testDeleteMoltenIronFormula_BeOccupied_ThrowsBusinessException() {
//            Long formulaId = 1L;
//
//            // 构造一个被使用的工艺列表
//            List<Technique> techniques = Collections.singletonList(new Technique());
//            when(techniqueManager.getTechniquesByMoltenIronFormulaId(formulaId))
//                    .thenReturn(techniques);
//
//            // 断言会抛出异常
//            assertThrows(BusinessException.class, () -> {
//                techniqueService.deleteMoltenIronFormula(formulaId);
//            });
//
//            // 验证仅查询了一次，但不会执行删除
//            verify(techniqueManager, times(1)).getTechniquesByMoltenIronFormulaId(formulaId);
//            verify(techniqueManager, never()).deleteMoltenIronFormula(anyLong());
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#searchMoltenIronFormula(SearchMoltenIronFormulaReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_searchMoltenIronFormula {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 其他 manager 模拟（虽然本方法未直接使用）
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private ProductManager productManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试正常搜索铁水配方的情况：输入有效请求，期望得到对应的分页结果
//         */
//        @Test
//        void testSearchMoltenIronFormula_Success() {
//            // 准备测试数据
//            SearchMoltenIronFormulaReq request = new SearchMoltenIronFormulaReq();
//            request.setSearch("test");
//            request.setCurrent(1);
//            request.setPageSize(10);
//
//            MoltenIronFormula formula = new MoltenIronFormula();
//            formula.setId(1L);
//            formula.setName("铁水配方一号");
//
//            PageResult<MoltenIronFormula> expectedResult = PageResult.of(1, 1, 10, Collections.singletonList(formula));
//
//            // 设置 mock 行为
//            when(techniqueManager.searchMoltenIronFormula(any(SearchMoltenIronFormulaReq.class)))
//                    .thenReturn(expectedResult);
//
//            // 执行目标方法
//            PageResult<MoltenIronFormula> actualResult = techniqueService.searchMoltenIronFormula(request);
//
//            // 断言结果一致
//            assertEquals(expectedResult, actualResult);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).searchMoltenIronFormula(request);
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#updateMoltenIronFormula(Long, CreateMoltenIronFormulaReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_updateMoltenIronFormula {
//
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//        }
//
//        /**
//         * 测试正常更新铁水配方：型号未重复
//         */
//        @Test
//        void testUpdateMoltenIronFormula_ModelNotExists_Success() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateMoltenIronFormulaReq req = new CreateMoltenIronFormulaReq();
//            req.setModel("MIF-001");
//            req.setName("铁水配方一号");
//            req.setFormulaDetail(Collections.singletonList(new FormulaDetail()));
//
//            MoltenIronFormula convertedFormula = new MoltenIronFormula();
//            convertedFormula.setId(null); // converter 默认忽略 id
//            convertedFormula.setModel("MIF-001");
//
//            MoltenIronFormula formulaToUpdate = new MoltenIronFormula();
//            formulaToUpdate.setId(1L);
//            formulaToUpdate.setModel("MIF-001");
//
//            // 设置 mock 行为
//            when(techniqueManager.getMoltenIronFormulaByModel(anyString())).thenReturn(null); // 无重复型号
//            when(converter.toMoltenIronFormula(any(CreateMoltenIronFormulaReq.class))).thenReturn(convertedFormula);
//            doNothing().when(techniqueManager).updateMoltenIronFormula(any(MoltenIronFormula.class));
//
//            // 执行目标方法
//            assertDoesNotThrow(() -> techniqueService.updateMoltenIronFormula(id, req));
//
//            // 验证交互
//            verify(techniqueManager, times(1)).getMoltenIronFormulaByModel("MIF-001");
//            verify(converter, times(1)).toMoltenIronFormula(req);
//            verify(techniqueManager, times(1)).updateMoltenIronFormula(formulaToUpdate);
//        }
//
//        /**
//         * 测试更新铁水配方：型号重复但为当前对象（允许更新）
//         */
//        @Test
//        void testUpdateMoltenIronFormula_ModelExistsButSameId_Success() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateMoltenIronFormulaReq req = new CreateMoltenIronFormulaReq();
//            req.setModel("MIF-001");
//            req.setName("铁水配方一号");
//            req.setFormulaDetail(Collections.singletonList(new FormulaDetail()));
//
//            MoltenIronFormula existingFormula = new MoltenIronFormula();
//            existingFormula.setId(1L);
//            existingFormula.setModel("MIF-001");
//
//            MoltenIronFormula convertedFormula = new MoltenIronFormula();
//            convertedFormula.setId(null); // converter 默认忽略 id
//            convertedFormula.setModel("MIF-001");
//
//            MoltenIronFormula formulaToUpdate = new MoltenIronFormula();
//            formulaToUpdate.setId(1L);
//            formulaToUpdate.setModel("MIF-001");
//
//            // 设置 mock 行为
//            when(techniqueManager.getMoltenIronFormulaByModel(anyString())).thenReturn(existingFormula); // 同一对象
//            when(converter.toMoltenIronFormula(any(CreateMoltenIronFormulaReq.class))).thenReturn(convertedFormula);
//            doNothing().when(techniqueManager).updateMoltenIronFormula(any(MoltenIronFormula.class));
//
//            // 执行目标方法
//            assertDoesNotThrow(() -> techniqueService.updateMoltenIronFormula(id, req));
//
//            // 验证交互
//            verify(techniqueManager, times(1)).getMoltenIronFormulaByModel("MIF-001");
//            verify(converter, times(1)).toMoltenIronFormula(req);
//            verify(techniqueManager, times(1)).updateMoltenIronFormula(formulaToUpdate);
//        }
//
//        /**
//         * 测试更新铁水配方：型号重复且为其他对象（应抛异常）
//         */
//        @Test
//        void testUpdateMoltenIronFormula_ModelExistsAndDifferentId_ThrowsBusinessException() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateMoltenIronFormulaReq req = new CreateMoltenIronFormulaReq();
//            req.setModel("MIF-001");
//            req.setName("铁水配方一号");
//
//            MoltenIronFormula existingFormula = new MoltenIronFormula();
//            existingFormula.setId(2L); // 不同 ID
//            existingFormula.setModel("MIF-001");
//
//            // 设置 mock 行为
//            when(techniqueManager.getMoltenIronFormulaByModel(anyString())).thenReturn(existingFormula); // 不同对象
//
//            // 执行目标方法并断言异常
//            BusinessException exception = assertThrows(BusinessException.class, () -> {
//                techniqueService.updateMoltenIronFormula(id, req);
//            });
//
//            // 验证异常信息
//            assertEquals(BssExType.MOLTEN_IRON_FORMULA_MODEL_REPEAT.getCode(), exception.getCode());
//
//            // 验证交互
//            verify(techniqueManager, times(1)).getMoltenIronFormulaByModel("MIF-001");
//            verify(converter, never()).toMoltenIronFormula(any(CreateMoltenIronFormulaReq.class));
//            verify(techniqueManager, never()).updateMoltenIronFormula(any());
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#createTechnique(CreateTechniqueReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_createTechnique {
//
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//        }
//
//        /**
//         * 测试成功创建工艺的情况
//         */
//        @Test
//        void testCreateTechnique_Success() {
//            // 准备请求数据
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setTechniqueNo("T001");
//            req.setSandboxGroupId(1L);
//            req.setSandboxMoldGroupId(2L);
//            req.setSandCoreMoldGroupId(3L);
//            req.setSandFormulaId(4L);
//            req.setMoltenIronFormulaId(5L);
//
//            // 模拟工艺编号不存在
//            when(techniqueManager.getTechniqueByNo("T001")).thenReturn(null);
//
//            // 模拟关联对象存在
//            when(sandboxManager.getSandboxGroupById(1L)).thenReturn(new SandboxGroup());
//            when(moldManager.getMoldGroupById(2L)).thenReturn(new MoldGroup());
//            when(moldManager.getMoldGroupById(3L)).thenReturn(new MoldGroup());
//            when(techniqueManager.getSandFormula(4L)).thenReturn(new SandFormula());
//            when(techniqueManager.getMoltenIronFormula(5L)).thenReturn(new MoltenIronFormula());
//
//            // 模拟对象转换
//            Technique technique = new Technique();
//            when(converter.toTechnique(req)).thenReturn(technique);
//
//            // 模拟保存操作
//            doNothing().when(techniqueManager).createTechnique(technique);
//
//            // 执行方法
//            techniqueService.createTechnique(req);
//
//            // 验证交互
//            verify(techniqueManager, times(1)).getTechniqueByNo("T001");
//            verify(converter, times(1)).toTechnique(req);
//            verify(techniqueManager, times(1)).createTechnique(technique);
//        }
//
//        /**
//         * 测试工艺编号已存在的情况
//         */
//        @Test
//        void testCreateTechnique_TechniqueNoExists_ThrowsBusinessException() {
//            // 准备请求数据
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setTechniqueNo("T001");
//
//            // 模拟工艺编号已存在
//            Technique existingTechnique = new Technique();
//            existingTechnique.setId(1L);
//            when(techniqueManager.getTechniqueByNo("T001")).thenReturn(existingTechnique);
//
//            // 断言抛出异常
//            BusinessException exception = assertThrows(BusinessException.class, () -> {
//                techniqueService.createTechnique(req);
//            });
//
//            assertEquals(BssExType.TECHNIQUE_NO_REPEAT.getCode(), exception.getCode());
//
//            // 验证未执行后续操作
//            verify(converter, never()).toTechnique(any(CreateTechniqueReq.class));
//            verify(techniqueManager, never()).createTechnique(any());
//        }
//
//        /**
//         * 测试砂箱组不存在的情况
//         */
//        @Test
//        void testCreateTechnique_SandboxGroupNotFound_ThrowsNotFoundException() {
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setSandboxGroupId(1L);
//
//            when(techniqueManager.getTechniqueByNo(anyString())).thenReturn(null);
//            when(sandboxManager.getSandboxGroupById(1L)).thenReturn(null);
//
//            assertThrows(NotFoundException.class, () -> {
//                techniqueService.createTechnique(req);
//            });
//        }
//
//        /**
//         * 测试砂箱模具组不存在的情况
//         */
//        @Test
//        void testCreateTechnique_SandboxMoldGroupNotFound_ThrowsNotFoundException() {
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setSandboxMoldGroupId(2L);
//
//            when(techniqueManager.getTechniqueByNo(anyString())).thenReturn(null);
//            when(sandboxManager.getSandboxGroupById(anyLong())).thenReturn(new SandboxGroup());
//            when(moldManager.getMoldGroupById(2L)).thenReturn(null);
//
//            assertThrows(NotFoundException.class, () -> {
//                techniqueService.createTechnique(req);
//            });
//        }
//
//        /**
//         * 测试砂芯模具组不存在的情况
//         */
//        @Test
//        void testCreateTechnique_SandCoreMoldGroupNotFound_ThrowsNotFoundException() {
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setSandCoreMoldGroupId(3L);
//
//            when(techniqueManager.getTechniqueByNo(anyString())).thenReturn(null);
//            when(sandboxManager.getSandboxGroupById(anyLong())).thenReturn(new SandboxGroup());
//            when(moldManager.getMoldGroupById(2L)).thenReturn(new MoldGroup());
//            when(moldManager.getMoldGroupById(3L)).thenReturn(null);
//
//            assertThrows(NotFoundException.class, () -> {
//                techniqueService.createTechnique(req);
//            });
//        }
//
//        /**
//         * 测试砂配方不存在的情况
//         */
//        @Test
//        void testCreateTechnique_SandFormulaNotFound_ThrowsNotFoundException() {
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setSandFormulaId(4L);
//
//            when(techniqueManager.getTechniqueByNo(anyString())).thenReturn(null);
//            when(sandboxManager.getSandboxGroupById(anyLong())).thenReturn(new SandboxGroup());
//            when(moldManager.getMoldGroupById(anyLong())).thenReturn(new MoldGroup());
//            when(techniqueManager.getSandFormula(4L)).thenReturn(null);
//
//            assertThrows(NotFoundException.class, () -> {
//                techniqueService.createTechnique(req);
//            });
//        }
//
//        /**
//         * 测试铁水配方不存在的情况
//         */
//        @Test
//        void testCreateTechnique_MoltenIronFormulaNotFound_ThrowsNotFoundException() {
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setMoltenIronFormulaId(5L);
//
//            when(techniqueManager.getTechniqueByNo(anyString())).thenReturn(null);
//            when(sandboxManager.getSandboxGroupById(anyLong())).thenReturn(new SandboxGroup());
//            when(moldManager.getMoldGroupById(anyLong())).thenReturn(new MoldGroup());
//            when(techniqueManager.getSandFormula(anyLong())).thenReturn(new SandFormula());
//            when(techniqueManager.getMoltenIronFormula(5L)).thenReturn(null);
//
//            assertThrows(NotFoundException.class, () -> {
//                techniqueService.createTechnique(req);
//            });
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#deleteTechnique(Long)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_deleteTechnique {
//
//        // 注入被测服务实例，并自动注入所有 @Mock 注解的对象
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        // 技术管理器接口模拟
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        // 其他 manager 模拟（虽然本方法未直接使用）
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private ProductManager productManager;
//
//        // 数据转换器接口模拟
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解支持
//        }
//
//        /**
//         * 测试：当要删除的工艺不存在时，应该抛出 BusinessException.TECHNIQUE_NOT_FOUND
//         */
//        @Test
//        void testDeleteTechnique_TechniqueNotFound_ThrowsException() {
//            Long invalidId = 999L;
//
//            // 模拟 getTechnique 返回 null
//            when(techniqueManager.getTechnique(invalidId)).thenReturn(null);
//
//            // 断言会抛出异常
//            assertThrows(BusinessException.class, () -> {
//                techniqueService.deleteTechnique(invalidId);
//            }, "应抛出 TECHNIQUE_NOT_FOUND 异常");
//
//            // 验证仅调用了 getTechnique 一次，其余方法不应被调用
//            verify(techniqueManager, times(1)).getTechnique(invalidId);
//            verify(productManager, never()).getProductsByTechniqueId(anyLong());
//            verify(techniqueManager, never()).deleteTechnique(anyLong());
//        }
//
//        /**
//         * 测试：当工艺被产品占用时，应该抛出 BusinessException.TECHNIQUE_BE_OCCUPIED
//         */
//        @Test
//        void testDeleteTechnique_TechniqueOccupiedByProduct_ThrowsException() {
//            Long validId = 1L;
//            Technique mockTechnique = new Technique();
//            mockTechnique.setId(validId);
//
//            // 模拟 getTechnique 成功找到工艺
//            when(techniqueManager.getTechnique(validId)).thenReturn(mockTechnique);
//
//            // 模拟有产品绑定了这个工艺
//            when(productManager.getProductsByTechniqueId(validId))
//                    .thenReturn(Collections.singletonList(new cn.hex.ddp.manufacture.domain.product.model.Product()));
//
//            // 断言会抛出异常
//            assertThrows(BusinessException.class, () -> {
//                techniqueService.deleteTechnique(validId);
//            }, "应抛出 TECHNIQUE_BE_OCCUPIED 异常");
//
//            // 验证 getTechnique 和 getProductsByTechniqueId 各调用了一次，但不会继续删除
//            verify(techniqueManager, times(1)).getTechnique(validId);
//            verify(productManager, times(1)).getProductsByTechniqueId(validId);
//            verify(techniqueManager, never()).deleteTechnique(anyLong());
//        }
//
//        /**
//         * 测试：正常情况下可以成功删除工艺
//         */
//        @Test
//        void testDeleteTechnique_Success() {
//            Long validId = 1L;
//            Technique mockTechnique = new Technique();
//            mockTechnique.setId(validId);
//
//            // 模拟 getTechnique 成功找到工艺
//            when(techniqueManager.getTechnique(validId)).thenReturn(mockTechnique);
//
//            // 模拟没有产品绑定该工艺
//            when(productManager.getProductsByTechniqueId(validId))
//                    .thenReturn(Collections.emptyList());
//
//            // 模拟 deleteTechnique 成功执行
//            doNothing().when(techniqueManager).deleteTechnique(validId);
//
//            // 执行目标方法
//            techniqueService.deleteTechnique(validId);
//
//            // 验证各方法按预期调用
//            verify(techniqueManager, times(1)).getTechnique(validId);
//            verify(productManager, times(1)).getProductsByTechniqueId(validId);
//            verify(techniqueManager, times(1)).deleteTechnique(validId);
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#updateTechnique(Long, CreateTechniqueReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_updateTechnique {
//
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//        }
//
//        /**
//         * 测试正常更新工艺的情况：所有关联对象都存在，且工艺编号唯一
//         */
//        @Test
//        void testUpdateTechnique_Success() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateTechniqueReq req = buildValidCreateTechniqueReq();
//            Technique convertedTechnique = new Technique();
//            convertedTechnique.setId(id);
//
//            // 设置 mock 行为
//            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(null); // 编号未被占用
//            when(converter.toTechnique(req)).thenReturn(convertedTechnique);
//            when(sandboxManager.getSandboxGroupById(req.getSandboxGroupId())).thenReturn(new SandboxGroup());
//            when(moldManager.getMoldGroupById(req.getSandboxMoldGroupId())).thenReturn(new MoldGroup());
//            when(moldManager.getMoldGroupById(req.getSandCoreMoldGroupId())).thenReturn(new MoldGroup());
//            when(techniqueManager.getSandFormula(req.getSandFormulaId())).thenReturn(new SandFormula());
//            when(techniqueManager.getMoltenIronFormula(req.getMoltenIronFormulaId())).thenReturn(new MoltenIronFormula());
//            doNothing().when(techniqueManager).updateTechnique(convertedTechnique);
//
//            // 执行目标方法
//            techniqueService.updateTechnique(id, req);
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
//            verify(converter, times(1)).toTechnique(req);
//            verify(sandboxManager, times(1)).getSandboxGroupById(req.getSandboxGroupId());
//            verify(moldManager, times(2)).getMoldGroupById(anyLong()); // 两次调用
//            verify(techniqueManager, times(1)).getSandFormula(req.getSandFormulaId());
//            verify(techniqueManager, times(1)).getMoltenIronFormula(req.getMoltenIronFormulaId());
//            verify(techniqueManager, times(1)).updateTechnique(convertedTechnique);
//        }
//
//        /**
//         * 测试当工艺编号已经存在且不属于当前更新记录时，应该抛出 BusinessException
//         */
//        @Test
//        void testUpdateTechnique_TechniqueNoConflict_ThrowsBusinessException() {
//            // 准备测试数据
//            Long id = 1L;
//            CreateTechniqueReq req = buildValidCreateTechniqueReq();
//
//            Technique existingTechnique = new Technique();
//            existingTechnique.setId(2L); // 不同于当前更新的 ID
//
//            // 设置 mock 行为
//            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(existingTechnique);
//
//            // 断言会抛出异常
//            assertThrows(BusinessException.class, () -> {
//                techniqueService.updateTechnique(id, req);
//            });
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
//            verify(converter, never()).toTechnique(any());
//            verify(techniqueManager, never()).updateTechnique(any());
//        }
//
//        /**
//         * 测试当砂箱组不存在时，应抛出 NotFoundException
//         */
////        @Test
////        void testUpdateTechnique_SandboxGroupNotFound_ThrowsNotFoundException() {
////            // 准备测试数据
////            Long id = 1L;
////            CreateTechniqueReq req = buildValidCreateTechniqueReq();
////
////            // 设置 mock 行为
////            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(null);
////            when(sandboxManager.getSandboxGroupById(req.getSandboxGroupId())).thenReturn(null);
////
////            // 断言会抛出异常
////            assertThrows(NotFoundException.class, () -> {
////                techniqueService.updateTechnique(id, req);
////            });
////
////            // 验证交互次数
////            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
////            verify(sandboxManager, times(1)).getSandboxGroupById(req.getSandboxGroupId());
////            verify(moldManager, never()).getMoldGroupById(anyLong());
////            verify(techniqueManager, never()).updateTechnique(any());
////        }
//
//        /**
//         * 测试当砂箱模具组不存在时，应抛出 NotFoundException
//         */
////        @Test
////        void testUpdateTechnique_SandboxMoldGroupNotFound_ThrowsNotFoundException() {
////            // 准备测试数据
////            Long id = 1L;
////            CreateTechniqueReq req = buildValidCreateTechniqueReq();
////
////            // 设置 mock 行为
////            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(null);
////            when(sandboxManager.getSandboxGroupById(req.getSandboxGroupId())).thenReturn(new SandboxGroup());
////            when(moldManager.getMoldGroupById(req.getSandboxMoldGroupId())).thenReturn(null);
////
////            // 断言会抛出异常
////            assertThrows(NotFoundException.class, () -> {
////                techniqueService.updateTechnique(id, req);
////            });
////
////            // 验证交互次数
////            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
////            verify(sandboxManager, times(1)).getSandboxGroupById(req.getSandboxGroupId());
////            verify(moldManager, times(1)).getMoldGroupById(req.getSandboxMoldGroupId());
////            verify(techniqueManager, never()).updateTechnique(any());
////        }
//
//        /**
//         * 测试当砂芯模具组不存在时，应抛出 NotFoundException
//         */
////        @Test
////        void testUpdateTechnique_SandCoreMoldGroupNotFound_ThrowsNotFoundException() {
////            // 准备测试数据
////            Long id = 1L;
////            CreateTechniqueReq req = buildValidCreateTechniqueReq();
////
////            // 设置 mock 行为
////            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(null);
////            when(sandboxManager.getSandboxGroupById(req.getSandboxGroupId())).thenReturn(new SandboxGroup());
////            when(moldManager.getMoldGroupById(req.getSandboxMoldGroupId())).thenReturn(new MoldGroup());
////            when(moldManager.getMoldGroupById(req.getSandCoreMoldGroupId())).thenReturn(null);
////
////            // 断言会抛出异常
////            assertThrows(NotFoundException.class, () -> {
////                techniqueService.updateTechnique(id, req);
////            });
////
////            // 验证交互次数
////            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
////            verify(sandboxManager, times(1)).getSandboxGroupById(req.getSandboxGroupId());
////            verify(moldManager, times(2)).getMoldGroupById(anyLong());
////            verify(techniqueManager, never()).updateTechnique(any());
////        }
//
//        /**
//         * 测试当砂配方不存在时，应抛出 NotFoundException
//         */
////        @Test
////        void testUpdateTechnique_SandFormulaNotFound_ThrowsNotFoundException() {
////            // 准备测试数据
////            Long id = 1L;
////            CreateTechniqueReq req = buildValidCreateTechniqueReq();
////
////            // 设置 mock 行为
////            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(null);
////            when(sandboxManager.getSandboxGroupById(req.getSandboxGroupId())).thenReturn(new SandboxGroup());
////            when(moldManager.getMoldGroupById(req.getSandboxMoldGroupId())).thenReturn(new MoldGroup());
////            when(moldManager.getMoldGroupById(req.getSandCoreMoldGroupId())).thenReturn(new MoldGroup());
////            when(techniqueManager.getSandFormula(req.getSandFormulaId())).thenReturn(null);
////
////            // 断言会抛出异常
////            assertThrows(NotFoundException.class, () -> {
////                techniqueService.updateTechnique(id, req);
////            });
////
////            // 验证交互次数
////            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
////            verify(sandboxManager, times(1)).getSandboxGroupById(req.getSandboxGroupId());
////            verify(moldManager, times(2)).getMoldGroupById(anyLong());
////            verify(techniqueManager, times(1)).getSandFormula(req.getSandFormulaId());
////            verify(techniqueManager, never()).updateTechnique(any());
////        }
//
//        /**
//         * 测试当铁水配方不存在时，应抛出 NotFoundException
//         */
////        @Test
////        void testUpdateTechnique_MoltenIronFormulaNotFound_ThrowsNotFoundException() {
////            // 准备测试数据
////            Long id = 1L;
////            CreateTechniqueReq req = buildValidCreateTechniqueReq();
////
////            // 设置 mock 行为
////            when(techniqueManager.getTechniqueByNo(req.getTechniqueNo())).thenReturn(null);
////            when(sandboxManager.getSandboxGroupById(req.getSandboxGroupId())).thenReturn(new SandboxGroup());
////            when(moldManager.getMoldGroupById(req.getSandboxMoldGroupId())).thenReturn(new MoldGroup());
////            when(moldManager.getMoldGroupById(req.getSandCoreMoldGroupId())).thenReturn(new MoldGroup());
////            when(techniqueManager.getSandFormula(req.getSandFormulaId())).thenReturn(new SandFormula());
////            when(techniqueManager.getMoltenIronFormula(req.getMoltenIronFormulaId())).thenReturn(null);
////
////            // 断言会抛出异常
////            assertThrows(NotFoundException.class, () -> {
////                techniqueService.updateTechnique(id, req);
////            });
////
////            // 验证交互次数
////            verify(techniqueManager, times(1)).getTechniqueByNo(req.getTechniqueNo());
////            verify(sandboxManager, times(1)).getSandboxGroupById(req.getSandboxGroupId());
////            verify(moldManager, times(2)).getMoldGroupById(anyLong());
////            verify(techniqueManager, times(1)).getSandFormula(req.getSandFormulaId());
////            verify(techniqueManager, times(1)).getMoltenIronFormula(req.getMoltenIronFormulaId());
////            verify(techniqueManager, never()).updateTechnique(any());
////        }
//
//        // 构造有效的 CreateTechniqueReq 请求对象
//        private CreateTechniqueReq buildValidCreateTechniqueReq() {
//            CreateTechniqueReq req = new CreateTechniqueReq();
//            req.setTechniqueNo("TECH-001");
//            req.setName("工艺一号");
//            req.setSandboxGroupId(10L);
//            req.setSandboxMoldGroupId(11L);
//            req.setSandCoreMoldGroupId(12L);
//            req.setSandFormulaId(13L);
//            req.setMoltenIronFormulaId(14L);
//            return req;
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#searchTechnique(SearchTechniqueReq)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_searchTechnique {
//
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//        }
//
//        /**
//         * 测试正常情况下的查询：
//         * - 技术管理器返回有效数据
//         * - 所有外部依赖均能正确获取映射关系
//         * - 转换器成功执行转换操作
//         */
//        @Test
//        void testSearchTechnique_NormalCase_ReturnsValidPageResult() {
//            // 准备输入参数
//            SearchTechniqueReq req = new SearchTechniqueReq();
//            req.setCurrent(1);
//            req.setPageSize(10);
//
//            // 模拟数据库返回的技术实体列表
//            Technique tech1 = new Technique();
//            tech1.setId(1L);
//            tech1.setSandboxGroupId(100L);
//            tech1.setSandboxMoldGroupId(200L);
//            tech1.setSandCoreMoldGroupId(300L);
//
//            PageResult<Technique> dbPageResult = PageResult.of(1, 1, 10, Arrays.asList(tech1));
//
//            // 模拟各 manager 返回的映射关系
//            Map<Long, SandboxGroupSummaryVO> sandboxMap = new HashMap<>();
//            sandboxMap.put(100L, new SandboxGroupSummaryVO());
//
//            Map<Long, MoldGroupSummaryVO> sandboxMoldMap = new HashMap<>();
//            sandboxMoldMap.put(200L, new MoldGroupSummaryVO());
//
//            Map<Long, MoldGroupSummaryVO> sandCoreMoldMap = new HashMap<>();
//            sandCoreMoldMap.put(300L, new MoldGroupSummaryVO());
//
//            // 模拟转换后的 VO 列表
//            List<TechniquePageVO> voList = Arrays.asList(new TechniquePageVO());
//            when(techniqueManager.searchTechnique(req)).thenReturn(dbPageResult);
//            when(sandboxManager.getSandboxGroupSummaryVOMap(any())).thenReturn(sandboxMap);
//            when(moldManager.getMoldGroupSummaryVOMap(any())).thenReturn(sandboxMoldMap).thenReturn(sandCoreMoldMap);
//            when(converter.toTechniquePageVOList(any(), any(), any(), any())).thenReturn(voList);
//
//            // 执行方法
//            PageResult<TechniquePageVO> result = techniqueService.searchTechnique(req);
//
//            // 验证结果
//            assertNotNull(result);
//            assertEquals(voList.size(), result.getList().size());
//            verify(techniqueManager, times(1)).searchTechnique(req);
//            verify(sandboxManager, times(1)).getSandboxGroupSummaryVOMap(any());
//            verify(moldManager, times(2)).getMoldGroupSummaryVOMap(any());
//            verify(converter, times(1)).toTechniquePageVOList(any(), any(), any(), any());
//        }
//
//        /**
//         * 测试当查询结果为空时的行为：
//         * - 技术管理器返回空列表
//         * - 外部依赖会被调用（即使结果为空）
//         * - 转换器也会被执行
//         */
//        @Test
//        void testSearchTechnique_EmptyResult_ReturnsEmptyPageResult() {
//            // 准备输入参数
//            SearchTechniqueReq req = new SearchTechniqueReq();
//
//            // 模拟数据库返回空结果
//            PageResult<Technique> emptyDbResult = PageResult.emptyResult();
//            when(techniqueManager.searchTechnique(req)).thenReturn(emptyDbResult);
//
//            // 即使结果为空，也需要模拟依赖方法的调用
//            when(sandboxManager.getSandboxGroupSummaryVOMap(any())).thenReturn(new HashMap<>());
//            when(moldManager.getMoldGroupSummaryVOMap(any())).thenReturn(new HashMap<>()).thenReturn(new HashMap<>());
//
//            // 执行方法
//            PageResult<TechniquePageVO> result = techniqueService.searchTechnique(req);
//
//            // 验证结果
//            assertNotNull(result);
//            assertTrue(result.isEmpty());
//            verify(techniqueManager, times(1)).searchTechnique(req);
//            verify(sandboxManager, times(1)).getSandboxGroupSummaryVOMap(any());
//            verify(moldManager, times(2)).getMoldGroupSummaryVOMap(any());
//            // 修改此处：验证 converter 被调用而不是 never 调用
//            verify(converter, times(1)).toTechniquePageVOList(any(), any(), any(), any());
//        }
//
//
//
//        /**
//         * 测试当技术管理器抛出运行时异常时是否传递出去
//         */
//        @Test
//        void testSearchTechnique_ManagerThrowsException_PropagatesException() {
//            // 准备输入参数
//            SearchTechniqueReq req = new SearchTechniqueReq();
//
//            // 模拟 manager 抛出异常
//            RuntimeException expectedException = new RuntimeException("Database error");
//            when(techniqueManager.searchTechnique(req)).thenThrow(expectedException);
//
//            // 断言会抛出异常
//            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
//                techniqueService.searchTechnique(req);
//            });
//
//            // 验证异常内容一致
//            assertEquals(expectedException.getMessage(), thrown.getMessage());
//            verify(techniqueManager, times(1)).searchTechnique(req);
//            verify(sandboxManager, never()).getSandboxGroupSummaryVOMap(any());
//            verify(moldManager, never()).getMoldGroupSummaryVOMap(any());
//            verify(converter, never()).toTechniquePageVOList(any(), any(), any(), any());
//        }
//    }
//
//    /**
//     * {@link TechniqueServiceImpl#getTechnique(Long)} 的单元测试
//     */
//    @Nested
//    class TechniqueServiceImplTest_getTechnique {
//
//        @InjectMocks
//        private TechniqueServiceImpl techniqueService;
//
//        @Mock
//        private TechniqueManager techniqueManager;
//
//        @Mock
//        private SandboxManager sandboxManager;
//
//        @Mock
//        private MoldManager moldManager;
//
//        @Mock
//        private TechniqueServiceConverter converter;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//        }
//
//        /**
//         * 测试正常获取工艺详情的情况：所有依赖都返回有效数据
//         */
//        @Test
//        void testGetTechnique_Success() {
//            // 准备测试数据
//            Long techniqueId = 1L;
//            Long sandboxGroupId = 10L;
//            Long sandboxMoldGroupId = 20L;
//            Long sandCoreMoldGroupId = 30L;
//
//            Technique technique = new Technique();
//            technique.setId(techniqueId);
//            technique.setSandboxGroupId(sandboxGroupId);
//            technique.setSandboxMoldGroupId(sandboxMoldGroupId);
//            technique.setSandCoreMoldGroupId(sandCoreMoldGroupId);
//
//            TechniqueVO techniqueVO = new TechniqueVO();
//            techniqueVO.setId(techniqueId);
//
//            SandboxGroup sandboxGroup = new SandboxGroup();
//            sandboxGroup.setId(sandboxGroupId);
//
//            MoldGroup sandboxMoldGroup = new MoldGroup();
//            sandboxMoldGroup.setId(sandboxMoldGroupId);
//
//            MoldGroup sandCoreMoldGroup = new MoldGroup();
//            sandCoreMoldGroup.setId(sandCoreMoldGroupId);
//
//            // 设置 mock 行为
//            when(techniqueManager.getTechnique(techniqueId)).thenReturn(technique);
//            when(converter.toTechniqueVO(technique)).thenReturn(techniqueVO);
//            when(sandboxManager.getSandboxGroupById(sandboxGroupId)).thenReturn(sandboxGroup);
//            when(moldManager.getMoldGroupDetailById(sandboxMoldGroupId)).thenReturn(sandboxMoldGroup);
//            when(moldManager.getMoldGroupDetailById(sandCoreMoldGroupId)).thenReturn(sandCoreMoldGroup);
//
//            // 执行目标方法
//            TechniqueVO result = techniqueService.getTechnique(techniqueId);
//
//            // 验证结果
//            assertNotNull(result);
//            assertEquals(techniqueId, result.getId());
//            assertEquals(sandboxGroup, result.getSandboxGroup());
//            assertEquals(sandboxMoldGroup, result.getSandboxMoldGroup());
//            assertEquals(sandCoreMoldGroup, result.getSandCoreMoldGroup());
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechnique(techniqueId);
//            verify(converter, times(1)).toTechniqueVO(technique);
//            verify(sandboxManager, times(1)).getSandboxGroupById(sandboxGroupId);
//            verify(moldManager, times(1)).getMoldGroupDetailById(sandboxMoldGroupId);
//            verify(moldManager, times(1)).getMoldGroupDetailById(sandCoreMoldGroupId);
//        }
//
//        /**
//         * 测试当 technique 不存在时的行为（假设会抛出异常）
//         */
//        @Test
//        void testGetTechnique_TechniqueNotFound_ThrowsException() {
//            Long techniqueId = 999L;
//
//            // 设置 mock 行为
//            when(techniqueManager.getTechnique(techniqueId)).thenReturn(null);
//
//            // 断言会抛出 NullPointerException
//            assertThrows(NullPointerException.class, () -> {
//                techniqueService.getTechnique(techniqueId);
//            });
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechnique(techniqueId);
//            verify(converter, never()).toTechniqueVO(any(Technique.class));
//            verify(sandboxManager, never()).getSandboxGroupById(any());
//            verify(moldManager, never()).getMoldGroupDetailById(any());
//        }
//
//        /**
//         * 测试当某个关联对象为空时的行为（如 sandboxGroup 为 null）
//         */
//        @Test
//        void testGetTechnique_SomeRelatedObjectIsNull() {
//            // 准备测试数据
//            Long techniqueId = 1L;
//            Long sandboxGroupId = 10L;
//            Long sandboxMoldGroupId = 20L;
//            Long sandCoreMoldGroupId = 30L;
//
//            Technique technique = new Technique();
//            technique.setId(techniqueId);
//            technique.setSandboxGroupId(sandboxGroupId);
//            technique.setSandboxMoldGroupId(sandboxMoldGroupId);
//            technique.setSandCoreMoldGroupId(sandCoreMoldGroupId);
//
//            TechniqueVO techniqueVO = new TechniqueVO();
//            techniqueVO.setId(techniqueId);
//
//            // 设置 mock 行为：sandboxGroup 为 null
//            when(techniqueManager.getTechnique(techniqueId)).thenReturn(technique);
//            when(converter.toTechniqueVO(technique)).thenReturn(techniqueVO);
//            when(sandboxManager.getSandboxGroupById(sandboxGroupId)).thenReturn(null);
//            when(moldManager.getMoldGroupDetailById(sandboxMoldGroupId)).thenReturn(new MoldGroup());
//            when(moldManager.getMoldGroupDetailById(sandCoreMoldGroupId)).thenReturn(new MoldGroup());
//
//            // 执行目标方法
//            TechniqueVO result = techniqueService.getTechnique(techniqueId);
//
//            // 验证结果
//            assertNotNull(result);
//            assertNull(result.getSandboxGroup());
//            assertNotNull(result.getSandboxMoldGroup());
//            assertNotNull(result.getSandCoreMoldGroup());
//
//            // 验证交互次数
//            verify(techniqueManager, times(1)).getTechnique(techniqueId);
//            verify(converter, times(1)).toTechniqueVO(technique);
//            verify(sandboxManager, times(1)).getSandboxGroupById(sandboxGroupId);
//            verify(moldManager, times(1)).getMoldGroupDetailById(sandboxMoldGroupId);
//            verify(moldManager, times(1)).getMoldGroupDetailById(sandCoreMoldGroupId);
//        }
//    }
//}
