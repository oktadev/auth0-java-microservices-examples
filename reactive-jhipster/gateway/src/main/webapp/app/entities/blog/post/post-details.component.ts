import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IPost } from '@/shared/model/blog/post.model';
import PostService from './post.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PostDetails extends mixins(JhiDataUtils) {
  @Inject('postService') private postService: () => PostService;
  @Inject('alertService') private alertService: () => AlertService;

  public post: IPost = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.postId) {
        vm.retrievePost(to.params.postId);
      }
    });
  }

  public retrievePost(postId) {
    this.postService()
      .find(postId)
      .then(res => {
        this.post = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
