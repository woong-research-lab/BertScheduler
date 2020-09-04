package kr.co.bizframe.bert.manager.model.api;

import java.util.List;

public class KorquadInput {

	private List<ParagraphsContext> data;

	public List<ParagraphsContext> getData() {
		return data;
	}

	public void setDatas(List<ParagraphsContext> data) {
		this.data = data;
	}

	public static class ParagraphsContext {
		private List<TrainingData> paragraphs;
		private String title;

		public List<TrainingData> getParagraphs() {
			return paragraphs;
		}

		public void setParagraphs(List<TrainingData> paragraphs) {
			this.paragraphs = paragraphs;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ParagraphsContext [paragraphs=");
			builder.append(paragraphs);
			builder.append(", title=");
			builder.append(title);
			builder.append("]");
			return builder.toString();
		}

	}

}
