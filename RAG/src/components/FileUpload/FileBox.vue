<template>
    <div class="wolfram-card" @click="handleClick">
        <div class="wolfram-card__icon-wrapper">
            <img :src="iconUrl" :alt="title + ' Icon'" class="wolfram-card__icon" />
        </div>
        <div class="wolfram-card__content">
            <h3 class="wolfram-card__title">{{ props.name }}</h3>
            <!-- <p class="wolfram-card__description">{{ description }}</p> -->
        </div>
    </div>

    <el-dialog :before-close="clickCancel" v-model="visiable" :title="props.name" size='80%' :with-header="false">
        <div class="file-cards-grid">
            <FileCard v-for="file in files" :key="file.id" :url="file.url" :fileContent="file.fileContent"
                deleteAble="false" />
        </div>
    </el-dialog>



</template>

<script setup>
import { ref, defineProps, computed, onMounted} from 'vue';
import { assets } from '@/assets/assets';
import FileCard from './FileCard.vue';
import BaseService from '@/service/BaseService';
import { ElNotification } from 'element-plus';

const props = defineProps({
    id: Number,
    name: String,
    type: String,
});

const visiable = ref(false);

const iconUrl = computed(() => {
    const iconName = `${props.type}_icon`;
    // console.log(iconName);
    return assets[iconName] || assets.Engineer_icon;
});
const title = props.title || 'Engineer';
const description = props.description || 'Engineer is great science';


onMounted(async () => {
    try {
        await BaseService.getBaseFiles({baseNum: props.id,page: 1, pageSize: 4});
    } catch (error) {
        ElNotification.error({
            message: "获取知识库列表失败"
        });
    }
});
const handleClick = () => {
    visiable.value = true;
}



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
]);
</script>

<style scoped>
.wolfram-card {
    display: flex;
    align-items: center;
    width: 400px;
    padding: 16px;
    border: 1px solid #e0e0e0;
    border-radius: 12px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    cursor: pointer;
    transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

.wolfram-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.wolfram-card__icon-wrapper {
    flex-shrink: 0;
    margin-right: 16px;
}

.wolfram-card__icon {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    object-fit: contain;
    background-color: #f0f0f0;
}

.wolfram-card__content {
    flex-grow: 1;
}

.wolfram-card__title {
    margin: 0 0 4px 0;
    font-size: 1.2em;
    color: #333;
    font-weight: 600;
}

.wolfram-card__description {
    margin: 0 0 8px 0;
    font-size: 0.9em;
    color: #666;
    line-height: 1.4;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
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