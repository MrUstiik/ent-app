package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.repository.CategoryRepository;
import com.mycompany.myapp.service.CategoryService;
import com.mycompany.myapp.service.dto.CategoryDTO;
import com.mycompany.myapp.service.mapper.CategoryMapper;
import com.mycompany.myapp.service.dto.CategoryCriteria;
import com.mycompany.myapp.service.CategoryQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CategoryResourceIT {

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MCC = "AAAA";
    private static final String UPDATED_MCC = "BBBB";

    private static final UUID DEFAULT_PARENT_CATEGORY_ID = UUID.randomUUID();
    private static final UUID UPDATED_PARENT_CATEGORY_ID = UUID.randomUUID();

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ICON_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_DEFAULT_ORDER_ID = 2;
    private static final Integer SMALLER_DEFAULT_ORDER_ID = 1 - 1;

    private static final ZonedDateTime DEFAULT_ADDED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ADDED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ADDED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_REGIONS = "AAAAAAAAAA";
    private static final String UPDATED_REGIONS = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private Category category;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .uuid(DEFAULT_UUID)
            .name(DEFAULT_NAME)
            .mcc(DEFAULT_MCC)
            .parentCategoryId(DEFAULT_PARENT_CATEGORY_ID)
            .enabled(DEFAULT_ENABLED)
            .iconUrl(DEFAULT_ICON_URL)
            .defaultOrderId(DEFAULT_DEFAULT_ORDER_ID)
            .addedDate(DEFAULT_ADDED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE)
            .regions(DEFAULT_REGIONS)
            .tags(DEFAULT_TAGS);
        return category;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category()
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .mcc(UPDATED_MCC)
            .parentCategoryId(UPDATED_PARENT_CATEGORY_ID)
            .enabled(UPDATED_ENABLED)
            .iconUrl(UPDATED_ICON_URL)
            .defaultOrderId(UPDATED_DEFAULT_ORDER_ID)
            .addedDate(UPDATED_ADDED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .regions(UPDATED_REGIONS)
            .tags(UPDATED_TAGS);
        return category;
    }

    @BeforeEach
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getMcc()).isEqualTo(DEFAULT_MCC);
        assertThat(testCategory.getParentCategoryId()).isEqualTo(DEFAULT_PARENT_CATEGORY_ID);
        assertThat(testCategory.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testCategory.getIconUrl()).isEqualTo(DEFAULT_ICON_URL);
        assertThat(testCategory.getDefaultOrderId()).isEqualTo(DEFAULT_DEFAULT_ORDER_ID);
        assertThat(testCategory.getAddedDate()).isEqualTo(DEFAULT_ADDED_DATE);
        assertThat(testCategory.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
        assertThat(testCategory.getRegions()).isEqualTo(DEFAULT_REGIONS);
        assertThat(testCategory.getTags()).isEqualTo(DEFAULT_TAGS);
    }

    @Test
    @Transactional
    public void createCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category with an existing ID
        category.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setUuid(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);


        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setName(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);


        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mcc").value(hasItem(DEFAULT_MCC)))
            .andExpect(jsonPath("$.[*].parentCategoryId").value(hasItem(DEFAULT_PARENT_CATEGORY_ID.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].iconUrl").value(hasItem(DEFAULT_ICON_URL)))
            .andExpect(jsonPath("$.[*].defaultOrderId").value(hasItem(DEFAULT_DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(sameInstant(DEFAULT_ADDED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].regions").value(hasItem(DEFAULT_REGIONS)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));
    }
    
    @Test
    @Transactional
    public void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mcc").value(DEFAULT_MCC))
            .andExpect(jsonPath("$.parentCategoryId").value(DEFAULT_PARENT_CATEGORY_ID.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.iconUrl").value(DEFAULT_ICON_URL))
            .andExpect(jsonPath("$.defaultOrderId").value(DEFAULT_DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.addedDate").value(sameInstant(DEFAULT_ADDED_DATE)))
            .andExpect(jsonPath("$.updatedDate").value(sameInstant(DEFAULT_UPDATED_DATE)))
            .andExpect(jsonPath("$.regions").value(DEFAULT_REGIONS))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS));
    }


    @Test
    @Transactional
    public void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        Long id = category.getId();

        defaultCategoryShouldBeFound("id.equals=" + id);
        defaultCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCategoriesByUuidIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where uuid equals to DEFAULT_UUID
        defaultCategoryShouldBeFound("uuid.equals=" + DEFAULT_UUID);

        // Get all the categoryList where uuid equals to UPDATED_UUID
        defaultCategoryShouldNotBeFound("uuid.equals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where uuid not equals to DEFAULT_UUID
        defaultCategoryShouldNotBeFound("uuid.notEquals=" + DEFAULT_UUID);

        // Get all the categoryList where uuid not equals to UPDATED_UUID
        defaultCategoryShouldBeFound("uuid.notEquals=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUuidIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where uuid in DEFAULT_UUID or UPDATED_UUID
        defaultCategoryShouldBeFound("uuid.in=" + DEFAULT_UUID + "," + UPDATED_UUID);

        // Get all the categoryList where uuid equals to UPDATED_UUID
        defaultCategoryShouldNotBeFound("uuid.in=" + UPDATED_UUID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where uuid is not null
        defaultCategoryShouldBeFound("uuid.specified=true");

        // Get all the categoryList where uuid is null
        defaultCategoryShouldNotBeFound("uuid.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name equals to DEFAULT_NAME
        defaultCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name not equals to DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the categoryList where name not equals to UPDATED_NAME
        defaultCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name is not null
        defaultCategoryShouldBeFound("name.specified=true");

        // Get all the categoryList where name is null
        defaultCategoryShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name contains DEFAULT_NAME
        defaultCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the categoryList where name contains UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name does not contain DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the categoryList where name does not contain UPDATED_NAME
        defaultCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCategoriesByMccIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where mcc equals to DEFAULT_MCC
        defaultCategoryShouldBeFound("mcc.equals=" + DEFAULT_MCC);

        // Get all the categoryList where mcc equals to UPDATED_MCC
        defaultCategoryShouldNotBeFound("mcc.equals=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    public void getAllCategoriesByMccIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where mcc not equals to DEFAULT_MCC
        defaultCategoryShouldNotBeFound("mcc.notEquals=" + DEFAULT_MCC);

        // Get all the categoryList where mcc not equals to UPDATED_MCC
        defaultCategoryShouldBeFound("mcc.notEquals=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    public void getAllCategoriesByMccIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where mcc in DEFAULT_MCC or UPDATED_MCC
        defaultCategoryShouldBeFound("mcc.in=" + DEFAULT_MCC + "," + UPDATED_MCC);

        // Get all the categoryList where mcc equals to UPDATED_MCC
        defaultCategoryShouldNotBeFound("mcc.in=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    public void getAllCategoriesByMccIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where mcc is not null
        defaultCategoryShouldBeFound("mcc.specified=true");

        // Get all the categoryList where mcc is null
        defaultCategoryShouldNotBeFound("mcc.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByMccContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where mcc contains DEFAULT_MCC
        defaultCategoryShouldBeFound("mcc.contains=" + DEFAULT_MCC);

        // Get all the categoryList where mcc contains UPDATED_MCC
        defaultCategoryShouldNotBeFound("mcc.contains=" + UPDATED_MCC);
    }

    @Test
    @Transactional
    public void getAllCategoriesByMccNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where mcc does not contain DEFAULT_MCC
        defaultCategoryShouldNotBeFound("mcc.doesNotContain=" + DEFAULT_MCC);

        // Get all the categoryList where mcc does not contain UPDATED_MCC
        defaultCategoryShouldBeFound("mcc.doesNotContain=" + UPDATED_MCC);
    }


    @Test
    @Transactional
    public void getAllCategoriesByParentCategoryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where parentCategoryId equals to DEFAULT_PARENT_CATEGORY_ID
        defaultCategoryShouldBeFound("parentCategoryId.equals=" + DEFAULT_PARENT_CATEGORY_ID);

        // Get all the categoryList where parentCategoryId equals to UPDATED_PARENT_CATEGORY_ID
        defaultCategoryShouldNotBeFound("parentCategoryId.equals=" + UPDATED_PARENT_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByParentCategoryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where parentCategoryId not equals to DEFAULT_PARENT_CATEGORY_ID
        defaultCategoryShouldNotBeFound("parentCategoryId.notEquals=" + DEFAULT_PARENT_CATEGORY_ID);

        // Get all the categoryList where parentCategoryId not equals to UPDATED_PARENT_CATEGORY_ID
        defaultCategoryShouldBeFound("parentCategoryId.notEquals=" + UPDATED_PARENT_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByParentCategoryIdIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where parentCategoryId in DEFAULT_PARENT_CATEGORY_ID or UPDATED_PARENT_CATEGORY_ID
        defaultCategoryShouldBeFound("parentCategoryId.in=" + DEFAULT_PARENT_CATEGORY_ID + "," + UPDATED_PARENT_CATEGORY_ID);

        // Get all the categoryList where parentCategoryId equals to UPDATED_PARENT_CATEGORY_ID
        defaultCategoryShouldNotBeFound("parentCategoryId.in=" + UPDATED_PARENT_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByParentCategoryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where parentCategoryId is not null
        defaultCategoryShouldBeFound("parentCategoryId.specified=true");

        // Get all the categoryList where parentCategoryId is null
        defaultCategoryShouldNotBeFound("parentCategoryId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where enabled equals to DEFAULT_ENABLED
        defaultCategoryShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the categoryList where enabled equals to UPDATED_ENABLED
        defaultCategoryShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllCategoriesByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where enabled not equals to DEFAULT_ENABLED
        defaultCategoryShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the categoryList where enabled not equals to UPDATED_ENABLED
        defaultCategoryShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllCategoriesByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultCategoryShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the categoryList where enabled equals to UPDATED_ENABLED
        defaultCategoryShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllCategoriesByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where enabled is not null
        defaultCategoryShouldBeFound("enabled.specified=true");

        // Get all the categoryList where enabled is null
        defaultCategoryShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByIconUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where iconUrl equals to DEFAULT_ICON_URL
        defaultCategoryShouldBeFound("iconUrl.equals=" + DEFAULT_ICON_URL);

        // Get all the categoryList where iconUrl equals to UPDATED_ICON_URL
        defaultCategoryShouldNotBeFound("iconUrl.equals=" + UPDATED_ICON_URL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByIconUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where iconUrl not equals to DEFAULT_ICON_URL
        defaultCategoryShouldNotBeFound("iconUrl.notEquals=" + DEFAULT_ICON_URL);

        // Get all the categoryList where iconUrl not equals to UPDATED_ICON_URL
        defaultCategoryShouldBeFound("iconUrl.notEquals=" + UPDATED_ICON_URL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByIconUrlIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where iconUrl in DEFAULT_ICON_URL or UPDATED_ICON_URL
        defaultCategoryShouldBeFound("iconUrl.in=" + DEFAULT_ICON_URL + "," + UPDATED_ICON_URL);

        // Get all the categoryList where iconUrl equals to UPDATED_ICON_URL
        defaultCategoryShouldNotBeFound("iconUrl.in=" + UPDATED_ICON_URL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByIconUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where iconUrl is not null
        defaultCategoryShouldBeFound("iconUrl.specified=true");

        // Get all the categoryList where iconUrl is null
        defaultCategoryShouldNotBeFound("iconUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByIconUrlContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where iconUrl contains DEFAULT_ICON_URL
        defaultCategoryShouldBeFound("iconUrl.contains=" + DEFAULT_ICON_URL);

        // Get all the categoryList where iconUrl contains UPDATED_ICON_URL
        defaultCategoryShouldNotBeFound("iconUrl.contains=" + UPDATED_ICON_URL);
    }

    @Test
    @Transactional
    public void getAllCategoriesByIconUrlNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where iconUrl does not contain DEFAULT_ICON_URL
        defaultCategoryShouldNotBeFound("iconUrl.doesNotContain=" + DEFAULT_ICON_URL);

        // Get all the categoryList where iconUrl does not contain UPDATED_ICON_URL
        defaultCategoryShouldBeFound("iconUrl.doesNotContain=" + UPDATED_ICON_URL);
    }


    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId equals to DEFAULT_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.equals=" + DEFAULT_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId equals to UPDATED_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.equals=" + UPDATED_DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId not equals to DEFAULT_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.notEquals=" + DEFAULT_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId not equals to UPDATED_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.notEquals=" + UPDATED_DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId in DEFAULT_DEFAULT_ORDER_ID or UPDATED_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.in=" + DEFAULT_DEFAULT_ORDER_ID + "," + UPDATED_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId equals to UPDATED_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.in=" + UPDATED_DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId is not null
        defaultCategoryShouldBeFound("defaultOrderId.specified=true");

        // Get all the categoryList where defaultOrderId is null
        defaultCategoryShouldNotBeFound("defaultOrderId.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId is greater than or equal to DEFAULT_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.greaterThanOrEqual=" + DEFAULT_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId is greater than or equal to UPDATED_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.greaterThanOrEqual=" + UPDATED_DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId is less than or equal to DEFAULT_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.lessThanOrEqual=" + DEFAULT_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId is less than or equal to SMALLER_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.lessThanOrEqual=" + SMALLER_DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId is less than DEFAULT_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.lessThan=" + DEFAULT_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId is less than UPDATED_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.lessThan=" + UPDATED_DEFAULT_ORDER_ID);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDefaultOrderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where defaultOrderId is greater than DEFAULT_DEFAULT_ORDER_ID
        defaultCategoryShouldNotBeFound("defaultOrderId.greaterThan=" + DEFAULT_DEFAULT_ORDER_ID);

        // Get all the categoryList where defaultOrderId is greater than SMALLER_DEFAULT_ORDER_ID
        defaultCategoryShouldBeFound("defaultOrderId.greaterThan=" + SMALLER_DEFAULT_ORDER_ID);
    }


    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate equals to DEFAULT_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.equals=" + DEFAULT_ADDED_DATE);

        // Get all the categoryList where addedDate equals to UPDATED_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate not equals to DEFAULT_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.notEquals=" + DEFAULT_ADDED_DATE);

        // Get all the categoryList where addedDate not equals to UPDATED_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.notEquals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate in DEFAULT_ADDED_DATE or UPDATED_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE);

        // Get all the categoryList where addedDate equals to UPDATED_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.in=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate is not null
        defaultCategoryShouldBeFound("addedDate.specified=true");

        // Get all the categoryList where addedDate is null
        defaultCategoryShouldNotBeFound("addedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate is greater than or equal to DEFAULT_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.greaterThanOrEqual=" + DEFAULT_ADDED_DATE);

        // Get all the categoryList where addedDate is greater than or equal to UPDATED_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.greaterThanOrEqual=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate is less than or equal to DEFAULT_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.lessThanOrEqual=" + DEFAULT_ADDED_DATE);

        // Get all the categoryList where addedDate is less than or equal to SMALLER_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.lessThanOrEqual=" + SMALLER_ADDED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate is less than DEFAULT_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.lessThan=" + DEFAULT_ADDED_DATE);

        // Get all the categoryList where addedDate is less than UPDATED_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.lessThan=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByAddedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where addedDate is greater than DEFAULT_ADDED_DATE
        defaultCategoryShouldNotBeFound("addedDate.greaterThan=" + DEFAULT_ADDED_DATE);

        // Get all the categoryList where addedDate is greater than SMALLER_ADDED_DATE
        defaultCategoryShouldBeFound("addedDate.greaterThan=" + SMALLER_ADDED_DATE);
    }


    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the categoryList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the categoryList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the categoryList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate is not null
        defaultCategoryShouldBeFound("updatedDate.specified=true");

        // Get all the categoryList where updatedDate is null
        defaultCategoryShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate is greater than or equal to DEFAULT_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.greaterThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the categoryList where updatedDate is greater than or equal to UPDATED_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.greaterThanOrEqual=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate is less than or equal to DEFAULT_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.lessThanOrEqual=" + DEFAULT_UPDATED_DATE);

        // Get all the categoryList where updatedDate is less than or equal to SMALLER_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.lessThanOrEqual=" + SMALLER_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate is less than DEFAULT_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.lessThan=" + DEFAULT_UPDATED_DATE);

        // Get all the categoryList where updatedDate is less than UPDATED_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.lessThan=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByUpdatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where updatedDate is greater than DEFAULT_UPDATED_DATE
        defaultCategoryShouldNotBeFound("updatedDate.greaterThan=" + DEFAULT_UPDATED_DATE);

        // Get all the categoryList where updatedDate is greater than SMALLER_UPDATED_DATE
        defaultCategoryShouldBeFound("updatedDate.greaterThan=" + SMALLER_UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllCategoriesByRegionsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where regions equals to DEFAULT_REGIONS
        defaultCategoryShouldBeFound("regions.equals=" + DEFAULT_REGIONS);

        // Get all the categoryList where regions equals to UPDATED_REGIONS
        defaultCategoryShouldNotBeFound("regions.equals=" + UPDATED_REGIONS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByRegionsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where regions not equals to DEFAULT_REGIONS
        defaultCategoryShouldNotBeFound("regions.notEquals=" + DEFAULT_REGIONS);

        // Get all the categoryList where regions not equals to UPDATED_REGIONS
        defaultCategoryShouldBeFound("regions.notEquals=" + UPDATED_REGIONS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByRegionsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where regions in DEFAULT_REGIONS or UPDATED_REGIONS
        defaultCategoryShouldBeFound("regions.in=" + DEFAULT_REGIONS + "," + UPDATED_REGIONS);

        // Get all the categoryList where regions equals to UPDATED_REGIONS
        defaultCategoryShouldNotBeFound("regions.in=" + UPDATED_REGIONS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByRegionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where regions is not null
        defaultCategoryShouldBeFound("regions.specified=true");

        // Get all the categoryList where regions is null
        defaultCategoryShouldNotBeFound("regions.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByRegionsContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where regions contains DEFAULT_REGIONS
        defaultCategoryShouldBeFound("regions.contains=" + DEFAULT_REGIONS);

        // Get all the categoryList where regions contains UPDATED_REGIONS
        defaultCategoryShouldNotBeFound("regions.contains=" + UPDATED_REGIONS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByRegionsNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where regions does not contain DEFAULT_REGIONS
        defaultCategoryShouldNotBeFound("regions.doesNotContain=" + DEFAULT_REGIONS);

        // Get all the categoryList where regions does not contain UPDATED_REGIONS
        defaultCategoryShouldBeFound("regions.doesNotContain=" + UPDATED_REGIONS);
    }


    @Test
    @Transactional
    public void getAllCategoriesByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where tags equals to DEFAULT_TAGS
        defaultCategoryShouldBeFound("tags.equals=" + DEFAULT_TAGS);

        // Get all the categoryList where tags equals to UPDATED_TAGS
        defaultCategoryShouldNotBeFound("tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTagsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where tags not equals to DEFAULT_TAGS
        defaultCategoryShouldNotBeFound("tags.notEquals=" + DEFAULT_TAGS);

        // Get all the categoryList where tags not equals to UPDATED_TAGS
        defaultCategoryShouldBeFound("tags.notEquals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where tags in DEFAULT_TAGS or UPDATED_TAGS
        defaultCategoryShouldBeFound("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS);

        // Get all the categoryList where tags equals to UPDATED_TAGS
        defaultCategoryShouldNotBeFound("tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where tags is not null
        defaultCategoryShouldBeFound("tags.specified=true");

        // Get all the categoryList where tags is null
        defaultCategoryShouldNotBeFound("tags.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByTagsContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where tags contains DEFAULT_TAGS
        defaultCategoryShouldBeFound("tags.contains=" + DEFAULT_TAGS);

        // Get all the categoryList where tags contains UPDATED_TAGS
        defaultCategoryShouldNotBeFound("tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where tags does not contain DEFAULT_TAGS
        defaultCategoryShouldNotBeFound("tags.doesNotContain=" + DEFAULT_TAGS);

        // Get all the categoryList where tags does not contain UPDATED_TAGS
        defaultCategoryShouldBeFound("tags.doesNotContain=" + UPDATED_TAGS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryShouldBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mcc").value(hasItem(DEFAULT_MCC)))
            .andExpect(jsonPath("$.[*].parentCategoryId").value(hasItem(DEFAULT_PARENT_CATEGORY_ID.toString())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].iconUrl").value(hasItem(DEFAULT_ICON_URL)))
            .andExpect(jsonPath("$.[*].defaultOrderId").value(hasItem(DEFAULT_DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(sameInstant(DEFAULT_ADDED_DATE))))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(sameInstant(DEFAULT_UPDATED_DATE))))
            .andExpect(jsonPath("$.[*].regions").value(hasItem(DEFAULT_REGIONS)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));

        // Check, that the count call also returns 1
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryShouldNotBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory
            .uuid(UPDATED_UUID)
            .name(UPDATED_NAME)
            .mcc(UPDATED_MCC)
            .parentCategoryId(UPDATED_PARENT_CATEGORY_ID)
            .enabled(UPDATED_ENABLED)
            .iconUrl(UPDATED_ICON_URL)
            .defaultOrderId(UPDATED_DEFAULT_ORDER_ID)
            .addedDate(UPDATED_ADDED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE)
            .regions(UPDATED_REGIONS)
            .tags(UPDATED_TAGS);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        restCategoryMockMvc.perform(put("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getMcc()).isEqualTo(UPDATED_MCC);
        assertThat(testCategory.getParentCategoryId()).isEqualTo(UPDATED_PARENT_CATEGORY_ID);
        assertThat(testCategory.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testCategory.getIconUrl()).isEqualTo(UPDATED_ICON_URL);
        assertThat(testCategory.getDefaultOrderId()).isEqualTo(UPDATED_DEFAULT_ORDER_ID);
        assertThat(testCategory.getAddedDate()).isEqualTo(UPDATED_ADDED_DATE);
        assertThat(testCategory.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
        assertThat(testCategory.getRegions()).isEqualTo(UPDATED_REGIONS);
        assertThat(testCategory.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void updateNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc.perform(put("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc.perform(delete("/api/categories/{id}", category.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
