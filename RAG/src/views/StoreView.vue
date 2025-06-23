<template>

    <div class="main">
        <div class="featured-section">
            <h1>知识库</h1>
            <p>最近上传的文件</p>
        </div>

        <div class="file-cards-grid">
            <FileCard v-for="file in files" :key="file.id" :url="file.url" :fileContent="file.fileContent"
                :deleteAble="file.deleteAble" />
        </div>

    </div>
</template>

<script setup>
import { ref } from 'vue';
import FileCard from '@/components/FileUpload/FileCard.vue';

console.log("hei"); // This log will still appear

// --- Data for your file cards ---
const files = ref([
    {
        id: 1, // Unique ID for key prop
        url: 'https://whu-chat.oss-cn-hangzhou.aliyuncs.com/uploads/cb9702b2-e7c3-48a2-b3c9-7fe2ee875683.docx',
        fileContent: 'WHU-实验报告 - 项目模板.docx-(38.3 KB)',
        deleteAble: false
    },
    {
        id: 2,
        url: 'https://example.com/another-document.pdf', // Example URL
        fileContent: '前端开发指南.pdf-(1.2 MB)',
        deleteAble: true // Example: make this one deletable
    },
    {
        id: 3,
        url: 'https://example.com/design-principles.pptx', // Example URL
        fileContent: '设计原则.pptx-(2.5 MB)',
        deleteAble: false
    },
    {
        id: 3,
        url: 'https://example.com/design-principles.pptx', // Example URL
        fileContent: '设计原则.pptx-(2.5 MB)',
        deleteAble: false
    },
    {
        id: 3,
        url: 'https://example.com/design-principles.pptx', // Example URL
        fileContent: '设计原则.pptx-(2.5 MB)',
        deleteAble: false
    },
    {
        id: 3,
        url: 'https://example.com/design-principles.pptx', // Example URL
        fileContent: '设计原则.pptx-(2.5 MB)',
        deleteAble: false
    },
    {
        id: 3,
        url: 'https://example.com/design-principles.pptx', // Example URL
        fileContent: '设计原则.pptx-(2.5 MB)',
        deleteAble: false
    },
]);
</script>

<style scoped>
.main {
    margin-top: 10vh;
    margin-left: 10vw;
    padding-left: 30%;
    padding-right: 30%;
    align-items: center;
    justify-content: baseline;
    justify-items: center;
}


header {
    margin-bottom: 20px;
}

h3 {
    font-size: 2em;
    color: #333;
    margin-bottom: 10px;
}

.featured-section {
    margin-top: 30px;
    margin-bottom: 20px;
    justify-items: center;
}

.featured-section h4 {
    font-size: 1.5em;
    color: #555;
    margin-bottom: 5px;
}

.featured-section p {
    font-size: 0.9em;
    color: #888;
}

.file-cards-grid {
    display: grid;

    /* For 2 columns: repeat(2, 1fr) */
    /* For responsive columns: repeat(auto-fill, minmax(280px, 1fr)) */

    grid-template-columns: repeat(2, 1fr);
    /* grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); */
    gap: 30px;
    padding: 20px 0;
}
</style>