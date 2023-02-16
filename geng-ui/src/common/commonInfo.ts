import { reactive, ref } from "vue";
import { Tag, TagNode } from "./../model/TagModel";

const isCollapse = ref(true);

const searchTags: TagNode[] = reactive([]);

const gengItemDialogOpen = ref(false);

export const commonInfo = {
  isCollapse,
  setIsCollapse: function (val: boolean) {
    isCollapse.value = val;
  },
  searchTags,
  addSearchTag: (tag: TagNode) => {
    const index = searchTags.findIndex((item) => item.id === tag.id);
    if (index === -1) {
      searchTags.push(tag);
    }
  },
  removeTag: (tag: TagNode) => {
    const index = searchTags.findIndex((item) => item.id === tag.id);
    if (index !== -1) {
      searchTags.splice(index, 1);
    }
  },
  gengItemDialogOpen: gengItemDialogOpen,
};
