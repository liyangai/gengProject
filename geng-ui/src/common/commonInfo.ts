import { HttpResult } from "@/model/HttpResultModel";
import { reactive, ref } from "vue";
import { Tag, TagNode } from "./../model/TagModel";
import { API } from "./axios";
import { mockTagList } from "@/assets/data/tagListData";
const isCollapse = ref(true);

const searchTags: TagNode[] = reactive([]);

const gengItemDialogOpen = ref(false);
const tagItemDialogOpen = ref(false);

const tagNodeList = reactive<TagNode[]>([]);

const refreshTagNodeList = () => {
  API.get("/tag")
    .then((res: any) => {
      const httpResult: HttpResult<Tag[]> = res.data;
      tagNodeList.length = 0;
      tagNodeList.push(...getTagNodeList(httpResult.data));
    })
    .catch(() => {
      tagNodeList.length = 0;
      tagNodeList.push(...getTagNodeList(mockTagList));
    });
};

const getTagNodeList = (list: Tag[]) => {
  const tempTagNodeList: TagNode[] = list.map((item) => {
    return new TagNode(item);
  });

  const tagTreeMap: Map<number, TagNode> = new Map<number, TagNode>();
  tempTagNodeList.forEach((item) => {
    tagTreeMap.set(item.id, item);
  });

  tempTagNodeList.forEach((item) => {
    if (item.parentId !== null) {
      const parentNode = tagTreeMap.get(item.parentId);
      if (parentNode != undefined) {
        item.parentTagName = parentNode.tagName;
        parentNode.children.push(item);
        parentNode.childrenNames.push(item.tagName);
      }
    }
  });
  return tempTagNodeList;
};

const setIsCollapse = (val: boolean) => {
  isCollapse.value = val;
};

const addSearchTag = (tag: TagNode) => {
  const index = searchTags.findIndex((item) => item.id === tag.id);
  if (index === -1) {
    searchTags.push(tag);
  }
};

const removeTag = (tag: TagNode) => {
  const index = searchTags.findIndex((item) => item.id === tag.id);
  if (index !== -1) {
    searchTags.splice(index, 1);
  }
};

export const commonInfo = {
  isCollapse,
  setIsCollapse,
  searchTags,
  addSearchTag,
  removeTag,
  gengItemDialogOpen,
  tagItemDialogOpen,
  tagNodeList,
  refreshTagNodeList,
};
