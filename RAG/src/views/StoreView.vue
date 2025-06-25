<template>
  <div class="main">
    <div class="featured-section">
      <h1>Knowledge Base</h1>
      <p>An investment in knowledge pays the best interest.</p>
      <p>Knowledge will bring you the opportunity to make a difference.</p>
    </div>

    <div class="file-boxs-grid">
      <FileBox
        v-for="box in boxList"
        :key="box.id"
        :id="box.id"
        :name="box.name"
        :type="box.type"
      />
    </div>

    <div class="pagination-controls-container">
      <el-pagination
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="1"      :layout="paginationLayout"
        :total="totalBoxes"  background
      >
      </el-pagination>
    </div>

    <AddBaseBtn @addBaseSuccess="handleAddBaseSuccess" />
  </div>
</template>

<script setup>
import { ref, computed ,onMounted} from 'vue';

import FileBox from '@/components/FileUpload/FileBox.vue';
import AddBaseBtn from '@/components/Base/AddBaseBtn.vue';
// 导入 Element Plus 分页组件，确保你在 main.js 或 vite.config.js 中正确引入和注册了 Element Plus
import { ElPagination,ElNotification } from 'element-plus';

import BaseService from '@/service/BaseService';

const files = ref([
  { id: 1, url: 'https://whu-chat.oss-cn-hangzhou.aliyuncs.com/uploads/cb9702b2-e7c3-48a2-b3c9-7fe2ee875683.docx', fileContent: 'WHU-实验报告 - 项目模板.docx-(38.3 KB)', deleteAble: false },
  { id: 2, url: 'https://example.com/another-document.pdf', fileContent: '前端开发指南.pdf-(1.2 MB)', deleteAble: true },
  { id: 3, url: 'https://example.com/design-principles.pptx', fileContent: '设计原则.pptx-(2.5 MB)', deleteAble: false },
  { id: 4, url: 'https://example.com/project-planning.docx', fileContent: '项目计划书.docx-(1.1 MB)', deleteAble: true },
  { id: 5, url: 'https://example.com/marketing-strategy.pdf', fileContent: '市场营销策略.pdf-(0.8 MB)', deleteAble: false },
  { id: 6, url: 'https://example.com/budget-report.xlsx', fileContent: '预算报告.xlsx-(0.5 MB)', deleteAble: true },
  { id: 7, url: 'https://example.com/team-meeting-notes.txt', fileContent: '团队会议记录.txt-(0.1 MB)', deleteAble: false },
  { id: 8, url: 'https://example.com/customer-feedback.csv', fileContent: '客户反馈.csv-(0.3 MB)', deleteAble: true },
  { id: 9, url: 'https://example.com/new-product-spec.pdf', fileContent: '新产品规格书.pdf-(1.8 MB)', deleteAble: false },
  { id: 10, url: 'https://example.com/hr-policy.docx', fileContent: '人力资源政策.docx-(0.7 MB)', deleteAble: true },
  { id: 11, url: 'https://example.com/meeting-agenda.pdf', fileContent: '会议议程.pdf-(0.2 MB)', deleteAble: false },
  { id: 12, url: 'https://example.com/sales-report.xlsx', fileContent: '销售报告.xlsx-(0.6 MB)', deleteAble: true },
  { id: 13, url: 'https://example.com/research-paper.pdf', fileContent: '研究论文.pdf-(3.1 MB)', deleteAble: false },
]);

const boxList = ref([])
const totalPage = ref(0)
const totalBoxes = ref(0)

onMounted(async () => {
    try {
        const res = await BaseService.getUserBaseInfo({page: 1 , pageSize: pageSize.value });
        // console.log(res)
        boxList.value = res.list;
        totalBoxes.value = res.total;
        totalPage.value = res.totalPage;

        console.log(boxList.value);

    }catch (error) {
        ElNotification.error({
            message: error.message,
        });
    }
})

const refresh = async () => {
  try {
    const res = await BaseService.getUserBaseInfo({ page: currentPage.value, pageSize: pageSize.value });
    boxList.value = res.list;
    totalBoxes.value = res.total;
    totalPage.value = res.totalPage;

  }catch{
    ElNotification.error({
      message: error.message,
    });
  }
}
const handleAddBaseSuccess = async () => {
  await refresh();
};

// --- 分页相关状态 ---
const currentPage = ref(1); // 当前页码，默认为第一页
const pageSize = ref(4); // 每页显示的文件数量，固定为4

// Element Plus 分页组件的布局
// 你可以根据需要调整，例如：'total, sizes, prev, pager, next, jumper'
const paginationLayout = ref('prev, pager, next'); // 只显示上一页、页码和下一页

// 计算当前页需要显示的文件
const paginatedFiles = computed(() => {
  const startIndex = (currentPage.value - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  return files.value.slice(startIndex, endIndex);
});

// 处理当前页码改变事件
const handleCurrentChange = async (newPage) => {
  currentPage.value = newPage;
  await refresh();
  // 由于是前端分页，这里不需要再次调用 API，数据已在 paginatedFiles 中更新
};

// 处理每页显示数量改变事件 (这里pageSize是固定值，但回调函数依然保留)
const handleSizeChange = (newSize) => {
  // 当每页大小改变时，通常重置回第一页
  // pageSize.value = newSize; // 如果 pageSize 是 ref 且可变
  // currentPage.value = 1;
  console.log('每页显示数量改变为:', newSize);
  // 如果 pageSize 是动态的，这里需要更新并可能重置 currentPage
};
</script>

<style scoped>
.main {
  margin-top: 10vh;
  margin-left: 10vw;
  padding-left: 20%;
  padding-right: 20%;
  box-sizing: border-box;
  display: flex;
  /* 使用 flex 布局来垂直居中内容 */
  flex-direction: column;
  align-items: center;
  /* 水平居中 */
  text-align: center;
  /* 文本居中 */
}

h1 {
  /* 假设是h1 */
  font-size: 2.5em;
  /* 调整字体大小 */
  color: #333;
  margin-bottom: 10px;
}

.featured-section {
  margin-top: 30px;
  margin-bottom: 20px;
  width: 100%;
  /* 确保占据整个宽度 */
}

.featured-section p {
  font-size: 1.1em;
  color: #666;
  line-height: 1.5;
}

.file-boxs-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  /* 固定两列 */
  gap: 30px;
  padding: 20px 0;
  /* 调整最大宽度以更好地居中，并与main的padding-left/right匹配 */
  max-width: 600px;
  /* 假设 FileBox 宽度 approx 280px * 2 + 30px gap */
  width: 100%;
  /* 确保在 max-width 范围内占据可用宽度 */
  justify-content: center;
  /* 如果列数不足，内容居中 */
}

.pagination-controls-container {
  margin-top: 30px;
  /* 与文件网格的间距 */
  margin-bottom: 30px;
  /* 与下方按钮的间距 */
  width: 100%;
  display: flex;
  justify-content: center;
  /* 使分页组件本身居中 */
}

/* 覆盖 Element Plus 默认样式，如果需要 */
.el-pagination {
  /* 例如，调整分页组件的字体大小 */
  font-size: 14px;
}

/* 如果你需要 Element Plus 文档中的 example-pagination-block 样式，
   可以将其复制过来或直接应用在 pagination-controls-container 上 */
.example-pagination-block+.example-pagination-block {
  margin-top: 10px;
  /* 如果有多个分页块 */
}

.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}
</style>
