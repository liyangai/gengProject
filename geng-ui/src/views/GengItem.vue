<template>
  <el-dialog v-model="dialogFormVisible" title="geng">
    <el-form :model="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="resume" :label-width="formLabelWidth">
            <el-input v-model="form.resume" autocomplete="off" />
          </el-form-item>
          <el-form-item label="tagNames" :label-width="formLabelWidth">
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
              v-for="tag in form.tagNames"
              :key="tag"
              class="tag-item"
              :type="getTagType(tag)"
              @close="deleteTag(tag)"
              closable
            >
              {{ tag }}
            </el-tag>
          </div>
          <div class="image-container">
            <img
              v-if="showImage()"
              :style="{ height: '200px' }"
              @click="openImage()"
              :src="imageSrcPrefix + form.src"
              alt=""
            />
          </div>
        </el-col>
        <el-col :span="12"
          ><el-form-item label="desc" :label-width="formLabelWidth">
            <el-input
              v-model="form.description"
              autocomplete="off"
              :rows="10"
              type="textarea"
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

    <el-dialog v-model="innerVisible" title="image">
      <template #default>
        <img :src="imageSrcPrefix + form.src" alt="" />
      </template>
    </el-dialog>
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

let dialogFormVisible = commonInfo.gengItemDialogOpen;

let tagSearch = ref("");

const imageSrcPrefix = "/temimage/";

const innerVisible = ref(false);
const tagNodeList: TagNode[] = commonInfo.tagNodeList;
interface Props {
  selectGeng: GengNode;
}

const formLabelWidth = "100px";
// const props = defineProps<Props>();
const props = withDefaults(defineProps<Props>(), {
  selectGeng: () => GengNode.getEmptyObj(),
});

const emit = defineEmits<{
  (e: "close", geng: GengNode): void;
}>();

const form = reactive(Object.assign({}, props.selectGeng));

const confirm = () => {
  const body: any = {
    data: form,
    tagNames: form.tagNames,
  };

  if (form.id === -1) {
    API.post("/geng", body).then((res) => {
      if (res == null) {
        return;
      }
      console.log(res);
      emit("close", form);
      dialogFormVisible.value = false;
    });
  } else {
    API.put("/geng", body).then((res) => {
      if (res == null) {
        return;
      }
      console.log(res);
      emit("close", form);
      dialogFormVisible.value = false;
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
    form.tagNames.push(tagName);
  }
  tagSearch.value = "";
};

const deleteTag = (tagName: string) => {
  const index = form.tagNames.findIndex((item) => item === tagName.trim());
  if (index > -1) {
    form.tagNames.splice(index, 1);
  }
};

const getTagType = (tagName: string) => {
  const index = tagNodeList.findIndex((item) => item.tagName === tagName);
  return index > -1 ? "" : "info";
};

const showImage = () => {
  return form.srcType === GengType.AUTO_ADD;
};

const openImage = () => {
  innerVisible.value = true;
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
