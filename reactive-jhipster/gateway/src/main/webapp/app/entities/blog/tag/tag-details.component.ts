import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITag } from '@/shared/model/blog/tag.model';
import TagService from './tag.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TagDetails extends Vue {
  @Inject('tagService') private tagService: () => TagService;
  @Inject('alertService') private alertService: () => AlertService;

  public tag: ITag = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tagId) {
        vm.retrieveTag(to.params.tagId);
      }
    });
  }

  public retrieveTag(tagId) {
    this.tagService()
      .find(tagId)
      .then(res => {
        this.tag = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
