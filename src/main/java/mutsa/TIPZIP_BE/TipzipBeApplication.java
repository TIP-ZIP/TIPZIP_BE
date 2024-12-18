package mutsa.TIPZIP_BE;

import mutsa.TIPZIP_BE.entity.Category;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaAuditing
public class TipzipBeApplication{

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:aws.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(TipzipBeApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);

		// 카테고리 세팅
		Category category1 = Category.builder().categoryName("정리/공간 활용").build();
		Category category2 = Category.builder().categoryName("주방").build();
		Category category3 = Category.builder().categoryName("청소").build();
		Category category4 = Category.builder().categoryName("건강").build();
		Category category5 = Category.builder().categoryName("IT").build();
		Category category6 = Category.builder().categoryName("뷰티&패션").build();
		Category category7 = Category.builder().categoryName("여가&휴식").build();
		Category category8 = Category.builder().categoryName("로컬").build();
		Category category9 = Category.builder().categoryName("기타").build();
	}
}
