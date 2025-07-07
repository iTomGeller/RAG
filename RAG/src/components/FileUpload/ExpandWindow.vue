<template>
  <div class="expandable-window-container">
    <div @click="toggleWindow">
      <slot name="button"></slot>
    </div>
    <!-- 展开的窗口 -->
    <transition name="expand">
      <div
        v-show="isOpen"
        class="window-content"
        :class="{ 'with-shadow': shadow }"
        :style="autoSize ? {} : { width: width, height: height }"
      >
        <!-- 窗口标题栏 -->
        <div class="window-header" @mousedown.stop="startDrag">
          <div class="window-title">{{ title }}</div>
          <button class="close-button" @click.stop="closeWindow">&times;</button>
        </div>

        <!-- 内容插槽 -->
        <div class="window-body">
          <slot name="content"></slot>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { gsap } from 'gsap' // 引入GSAP

const windowRef = ref(null)

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },

  autoSize: {
    type: Boolean,
    default: false,
  },

  // 窗口标题
  title: {
    type: String,
    default: 'Window Title',
  },
  // 初始是否展开
  initialOpen: {
    type: Boolean,
    default: false,
  },
  // 窗口宽度
  width: {
    type: String,
    default: '400px',
  },
  // 窗口高度
  height: {
    type: String,
    default: '300px',
  },
  // 是否显示阴影
  shadow: {
    type: Boolean,
    default: true,
  },
  // 是否可拖动
  draggable: {
    type: Boolean,
    default: true,
  },
})

const emit = defineEmits(['open', 'close', 'toggle'])

const isOpen = ref(props.initialOpen)
const isDragging = ref(false)
const startPos = ref({ x: 0, y: 0 })
const currentPos = ref({ x: 0, y: 0 })

const toggleWindow = () => {
  isOpen.value = !isOpen.value
  emit('toggle', isOpen.value)
  if (isOpen.value) {
    currentPos.value = { x: 0, y: 0 }
    emit('open')
  } else {
    emit('close')
  }
}

const openWindow = () => {
  isOpen.value = true
  currentPos.value = { x: 0, y: 0 }
  emit('open')
}

const closeWindow = () => {
  isOpen.value = false
  emit('close')
}

// 拖动相关逻辑
const startDrag = (e) => {
  if (!props.draggable) return

  isDragging.value = true
  startPos.value = {
    x: e.clientX - currentPos.value.x,
    y: e.clientY - currentPos.value.y,
  }

  document.addEventListener('mousemove', handleDrag)
  document.addEventListener('mouseup', stopDrag)
}

const handleDrag = (e) => {
  if (!isDragging.value) return

  currentPos.value = {
    x: e.clientX - startPos.value.x,
    y: e.clientY - startPos.value.y,
  }
}

const stopDrag = () => {
  isDragging.value = false
  document.removeEventListener('mousemove', handleDrag)
  document.removeEventListener('mouseup', stopDrag)
}

// 计算窗口位置样式
const windowStyle = computed(() => {
  return {
    transform: `translate(${currentPos.value.x}px, ${currentPos.value.y}px)`,
  }
})
</script>

<style scoped>
.expandable-window-container {
  user-select: none;
  position: relative;
  display: inline-block;
}

.window-content {
  position: absolute;
  top: -80%;
  left: -30%;
  margin-top: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  overflow: hidden;
  z-index: 1000;
  transform: v-bind('windowStyle.transform');
}
body.dark .window-content {
  border-color: #555;
}

.with-shadow {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.window-header {
  padding: 10px 15px;
  background-color: #f5f5f5;
  border-bottom: 1px solid #ddd;
  cursor: move;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
body.dark .window-header {
  background-color: #333;
}

.window-title {
  font-weight: bold;
}

.close-button {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #777;
  padding: 0 5px;
}

.close-button:hover {
  color: #333;
}

.window-body {
  padding: 15px;
  height: calc(100% - 42px); /* 减去标题栏高度 */
  background-color: #fff;
  overflow: auto;
}
body.dark .window-body {
  background-color: #333;
}

/* 展开动画 */
.expand-enter-active,
.expand-leave-active {
  transition:
    opacity 0.3s ease,
    transform 0.3s ease;
}

.expand-enter-from,
.expand-leave-to {
  opacity: 0;
  transform: translateY(-10px) v-bind('windowStyle.transform');
}
</style>
