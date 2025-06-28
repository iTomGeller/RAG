<template>
    <div class="wolfram-card" @click="handleClick">
        <div class="wolfram-card__icon-wrapper">
            <img :src="iconUrl" :alt="title + ' Icon'" class="wolfram-card__icon" />
        </div>
        <div class="wolfram-card__content">
            <h3 class="wolfram-card__title">{{ title }}</h3>
            <p class="wolfram-card__description">{{ description }}</p>
        </div>
    </div>

    <el-dialog :before-close="clickCancel" v-model="visiable" title="Create new knowledge base" size='80%'
        :with-header="false">
        <el-form :model="form" label-width="auto" class="form" label-position="top">
            <el-form-item label="Base name">
                <el-input style="width:60%" v-model="baseName" />
            </el-form-item>

            <el-form-item label="Base type">
                <el-select v-model="baseType" placeholder="Knowledge base type" size="large" style="width: 240px">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="handleCheck">Check</el-button>
                <el-button type="primary" @click="handleCancel">Cancel</el-button>
            </el-form-item>

        </el-form>

    </el-dialog>
</template>

<script setup>
import { assets } from '@/assets/assets';
import { ref } from 'vue';
import BaseService from '@/service/BaseService';
import { ElNotification } from 'element-plus';

const iconUrl = assets.new_box_icon
const title = 'Create Knowledge Base'
const visiable = ref(false)

const baseName = ref("")
const baseType = ref()

const emit = defineEmits(['addBaseSuccess'])

const clear = () => {
    baseName.value = ""
    baseType.value = ""
}

const close = () => {
    visiable.value = false;
    clear()
}


const handleClick = () => {
    visiable.value = true;
}

const handleCheck = async () => {
    if (!baseName.value || !baseType.value) {
        ElNotification.error({
            message: 'Please fill in all fields',
        })
        return
    }

    try {
        await BaseService.addBase({
            name: baseName.value,
            type: baseType.value
        })
        ElNotification.success({
            message: 'Create knowledge base successfully',
        })
        emit('addBaseSuccess')
        close()
    } catch (error) {
        ElNotification.error({
            message: 'Create knowledge base failed',
        })
    }
}

const handleCancel = () => {
    visiable.value = false
    close()
}


const options = [
    {
        value: 'Default',
        label: 'Default',
    },
    {
        value: 'Humanities',
        label: 'Humanities',
    },
    {
        value: 'Science',
        label: 'Science',
    },
    {
        value: 'Engineering',
        label: 'Engineering',
    },
    {
        value: 'Health',
        label: 'Health',
    },
    {
        value: 'Social',
        label: 'Social',
    }
]
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

.form {
    padding: 5%;
    max-width: 60vw;
}
</style>