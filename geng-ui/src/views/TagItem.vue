<template>
  <el-dialog v-model="dialogFormVisible" title="tag">
    <el-form :model="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="tagName" :label-width="formLabelWidth">
            <el-input v-model="form.tagName" autocomplete="off" />
          </el-form-item>
          <el-form-item label="childrenTags" :label-width="formLabelWidth">
            <el-autocomplete
              v-model="tagSearch"
              :fetch-suggestions="querySearch"
              clearable
              value-key="tagName"
              class="inline-input w-50"
              placeholder="Please Input"
              :select-when-unmatched="true"
              @select="handleSelect"
            />
          </el-form-item>
          <div class="slectTagList">
            <el-tag
              v-for="tag in form.childrenNames"
              :key="tag"
              class="tag-item"
              :type="getTagType(tag)"
              @close="deleteTag(tag)"
              closable
            >
              {{ tag }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="12"
          ><el-form-item label="parentTag" :label-width="formLabelWidth">
            <el-autocomplete
              v-model="form.parentTagName"
              :fetch-suggestions="querySearch"
              clearable
              value-key="tagName"
              class="inline-input w-50"
              placeholder="Please Input"
              :select-when-unmatched="true"
              @select="handleSelect"
            /> </el-form-item
        ></el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirm"> Confirm </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { API } from "@/common/axios";
import { commonInfo } from "@/common/commonInfo";
import { GengType } from "@/common/constant/GengType";
import { TagTypeConstant } from "@/common/constant/TagType";
import { Geng, GengNode } from "@/model/GengModel";
import { HttpResult } from "@/model/HttpResultModel";
import { Tag, TagNode } from "@/model/TagModel";
import { ElTree, ElInput, formProps } from "element-plus";
import {
  reactive,
  ref,
  watch,
  defineProps,
  withDefaults,
  defineEmits,
} from "vue";

let dialogFormVisible = commonInfo.tagItemDialogOpen;

let tagSearch = ref("");

const innerVisible = ref(false);
const tagNodeList: TagNode[] = commonInfo.tagNodeList;
interface Props {
  selectedTag: TagNode;
}

const formLabelWidth = "100px";
// const props = defineProps<Props>();
const props = withDefaults(defineProps<Props>(), {
  selectedTag: () => TagNode.getEmptyObj(),
});

const emit = defineEmits<{
  (e: "close", tag: TagNode): void;
}>();

const form = reactive(Object.assign({}, props.selectedTag));

const confirm = () => {
  dialogFormVisible.value = false;
  if (form.id === -1) {
    API.post("/tag", form).then((res) => {
      console.log(res);
      emit("close", form);
    });
  } else {
    API.post("/tag", form).then((res) => {
      console.log(res);
      emit("close", form);
    });
  }
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const querySearch = (queryString: string, cb: any) => {
  const results = queryString
    ? tagNodeList.filter((item) =>
        item.tagName.toLowerCase().includes(queryString.toLowerCase())
      )
    : tagNodeList;
  // call callback function to return suggestions
  cb(results);
};

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const handleSelect = (item: any) => {
  if (!item) {
    return;
  }

  let tagName = "";

  if (item.tagName) {
    tagName = item.tagName;
  } else {
    tagName = item.value;
  }

  tagName = tagName.trim();
  if (tagName) {
    deleteTag(tagName);
    form.childrenNames.push(tagName);
  }
};

const deleteTag = (tagName: string) => {
  const index = form.childrenNames.findIndex((item) => item === tagName.trim());
  if (index > -1) {
    form.childrenNames.splice(index, 1);
  }
};

const getTagType = (tagName: string) => {
  const index = tagNodeList.findIndex((item) => item.tagName === tagName);
  return index > -1 ? "" : "info";
};
</script>

<style lang="less" scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.el-dropdown-link {
  cursor: pointer;
}

.tag-container {
  height: 100%;
  padding: 10px;
}

.tag-item {
  margin-bottom: 10px;
}
</style>
